package com.example.simplemedicplan.feature.home.pills

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.simplemedicplan.model.home.PillDescription
import com.example.simplemedicplan.model.home.PillDescriptionUI
import com.example.simplemedicplan.utils.FIREBASE_DATABASE_PILLS_DESCRIPTION
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
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
    }

    override fun onAddButtonClick() {
        events.trySend(PillsEvents.NavigateToAddPill)
    }
}