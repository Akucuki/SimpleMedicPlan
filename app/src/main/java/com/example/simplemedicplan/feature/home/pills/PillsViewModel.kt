package com.example.simplemedicplan.feature.home.pills

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.simplemedicplan.model.home.PillDescription
import com.example.simplemedicplan.model.home.PillDescriptionUI
import com.example.simplemedicplan.utils.FIREBASE_DATABASE_PILLS_DESCRIPTION
import com.example.simplemedicplan.utils.decodeToLocalDateTime
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import java.time.LocalDateTime
import javax.inject.Inject

private const val IS_LOADING_IN_PROGRESS = "is_loading_in_progress"
private const val PILLS_DESCRIPTIONS = "pills_descriptions"

@HiltViewModel
class PillsViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    databaseReference: DatabaseReference
) : ViewModel(), PillsViewModelMock {

    override val events = Channel<PillsEvents>(Channel.UNLIMITED)
    val pillsDescriptions = handle.getStateFlow(PILLS_DESCRIPTIONS, emptyList<PillDescriptionUI>())
    val isLoadingInProgress = handle.getStateFlow(IS_LOADING_IN_PROGRESS, false)

    private val pillsDatabaseNodeReference = databaseReference
        .child(FIREBASE_DATABASE_PILLS_DESCRIPTION)

    init {
        fetchPillsDescriptions()
    }

    fun fetchPillsDescriptions() {
        handle[IS_LOADING_IN_PROGRESS] = true
        pillsDatabaseNodeReference.get()
            .addOnSuccessListener {
                val pillsDescriptions = it.children.map { snapshot ->
                    snapshot.getValue(PillDescription::class.java)!!.toUI()
                }
                handle[PILLS_DESCRIPTIONS] = pillsDescriptions
                removeOldReminders()
                registerReminders()
            }
            .addOnFailureListener {
                Log.d("vitalik", "Failed to fetch the pills descriptions: $it")
            }
            .addOnCompleteListener { handle[IS_LOADING_IN_PROGRESS] = false }
    }

    fun onPillCardClick(pillDescription: PillDescriptionUI) {
        handle[PILLS_DESCRIPTIONS] = pillsDescriptions.value.map { current ->
            if (current == pillDescription) {
                current.copy(isExpanded = !current.isExpanded)
            } else {
                current
            }
        }
    }

    fun onPillCardEditClick(uuid: String) {
        events.trySend(PillsEvents.NavigateToEditPill(uuid))
    }

    fun onPillCardRemoveClick(pillDescription: PillDescriptionUI) {
        pillsDatabaseNodeReference.child(pillDescription.uuid).removeValue()
            .addOnSuccessListener {
                handle[PILLS_DESCRIPTIONS] = pillsDescriptions.value.filter { current ->
                    current != pillDescription
                }
            }
            .addOnFailureListener {
                Log.d("vitalik", "Failed to remove the pill description: $it")
            }
        handle[PILLS_DESCRIPTIONS] = pillsDescriptions.value - pillDescription
        pillDescription.remaindersDates.forEach { date ->
            events.trySend(
                PillsEvents.DismissReminder(
                    date = date.decodeToLocalDateTime(),
                    notificationTag = PillDescription.getNotificationId(pillDescription.uuid, date),
                    pillName = pillDescription.name,
                    pillDosage = pillDescription.dosage,
                    pillDosageType = pillDescription.dosageType
                )
            )
        }
    }

    override fun onAddButtonClick() {
        events.trySend(PillsEvents.NavigateToEditPill())
    }

    private fun removeOldReminders() {
        pillsDescriptions.value.forEach { pillDescription ->
            pillDescription.remaindersDates.forEach { date ->
                if (date.decodeToLocalDateTime() <= LocalDateTime.now()) {
                    removeRemoteReminderDate(pillDescription, date)
                }
            }
        }
    }

    private fun registerReminders() {
        pillsDescriptions.value.forEach {
            it.remaindersDates.forEach { encodedDateTime ->
                events.trySend(
                    PillsEvents.RegisterReminder(
                        date = encodedDateTime.decodeToLocalDateTime(),
                        notificationTag = PillDescription.getNotificationId(it.uuid, encodedDateTime),
                        pillDosage = it.dosage,
                        pillName = it.name,
                        pillDosageType = it.dosageType
                    )
                )
            }
        }
    }

    private fun removeRemoteReminderDate(pillDescription: PillDescriptionUI, encodedDate: String) {
        pillsDatabaseNodeReference.child(pillDescription.uuid).setValue(
            pillDescription.copy(remaindersDates = pillDescription.remaindersDates - encodedDate)
        )
    }
}