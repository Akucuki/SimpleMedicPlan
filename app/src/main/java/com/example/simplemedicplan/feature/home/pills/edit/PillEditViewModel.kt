package com.example.simplemedicplan.feature.home.pills.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemedicplan.model.TextFieldValueWrapper
import com.example.simplemedicplan.model.home.PillDescription
import com.example.simplemedicplan.model.home.PillDosageType
import com.example.simplemedicplan.utils.FIREBASE_DATABASE_PILLS_DESCRIPTION
import com.example.simplemedicplan.utils.InputValidator
import com.example.simplemedicplan.utils.decodeToLocalDateTimeCollection
import com.example.simplemedicplan.utils.encodeToStringsCollection
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

private const val EXISTING_PILL_UUID = "existing_pill_uuid"

private const val NAME = "name"
private const val DOSAGE_TYPE = "dosage_type"
private const val IS_DOSAGE_DROPDOWN_EXPANDED = "is_dosage_dropdown_expanded"
private const val DOSAGE = "dosage"
private const val NOTES = "notes"
private const val IS_REMINDER_ENABLED = "reminder_enabled"
private const val IS_SAVE_CHANGES_DIALOG_VISIBLE = "is_save_changes_dialog_visible"
private const val REMINDER_DATES = "reminder_dates"

@HiltViewModel
class PillEditViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    databaseReference: DatabaseReference
) : ViewModel() {

    val events = Channel<PillEditEvents>(Channel.UNLIMITED)

    private val existingPillUuid = handle.getStateFlow<String?>(EXISTING_PILL_UUID, null)

    // Fields values
    val name = handle.getStateFlow(NAME, TextFieldValueWrapper())
    val dosage = handle.getStateFlow(DOSAGE, TextFieldValueWrapper(value = "1.0"))
    val notes = handle.getStateFlow(NOTES, TextFieldValueWrapper())
    val isReminderEnabled = handle.getStateFlow(IS_REMINDER_ENABLED, false)

    // Dropdowns values
    val selectedDosageType = handle.getStateFlow(DOSAGE_TYPE, PillDosageType.MILLIGRAMS)

    // Visibility values
    val isDosageDropdownExpanded = handle.getStateFlow(IS_DOSAGE_DROPDOWN_EXPANDED, false)
    val isSaveChangesDialogVisible = handle.getStateFlow(IS_SAVE_CHANGES_DIALOG_VISIBLE, false)

    // Dates management
    val reminderDates =
        handle.getStateFlow(REMINDER_DATES, setOf<String>()).map { stringEncodedDates ->
            stringEncodedDates.decodeToLocalDateTimeCollection().toSet()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptySet())

    private val pillsDatabaseNodeReference = databaseReference
        .child(FIREBASE_DATABASE_PILLS_DESCRIPTION)

    fun onExistingPillUuidObtained(uuid: String) {
        handle[EXISTING_PILL_UUID] = uuid
        pillsDatabaseNodeReference.child(uuid).get().addOnSuccessListener { dataSnapshot ->
            val pillDescription = dataSnapshot.getValue(PillDescription::class.java)
                ?: return@addOnSuccessListener
            handle[NAME] = name.value.copy(value = pillDescription.name)
            handle[DOSAGE_TYPE] = pillDescription.dosageType
            handle[DOSAGE] = dosage.value.copy(value = pillDescription.dosage.toString())
            handle[NOTES] = notes.value.copy(value = pillDescription.notes)
            handle[IS_REMINDER_ENABLED] = pillDescription.remaindersDates.isNotEmpty()
            handle[REMINDER_DATES] = pillDescription.remaindersDates.toSet()
        }
    }

    fun onNameChange(input: String) {
        handle[NAME] = name.value.copy(value = input, errorId = null)
    }

    fun onDosageDropdownExpandClick() {
        handle[IS_DOSAGE_DROPDOWN_EXPANDED] = true
    }

    fun onDosageTypeSelect(dosageType: PillDosageType) {
        handle[DOSAGE_TYPE] = dosageType
        onDosageDropdownDismiss()
    }

    fun onDosageDropdownDismiss() {
        handle[IS_DOSAGE_DROPDOWN_EXPANDED] = false
    }

    fun onDosageChange(input: String) {
        handle[DOSAGE] = dosage.value.copy(value = input)
    }

    fun onNotesChange(input: String) {
        handle[NOTES] = notes.value.copy(value = input)
    }

    fun onIsReminderEnabledChange(isEnabled: Boolean) {
        handle[IS_REMINDER_ENABLED] = isEnabled
    }

    fun onBackClick() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = true
    }

    fun onSaveChangesDialogConfirmClick() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = false
        onSaveClick()
    }

    fun onSaveChangesDialogDiscardClick() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = false
        events.trySend(PillEditEvents.NavigateBack)
    }

    fun onSaveClick() {
        val nameErrorId = InputValidator.getMedicineNameErrorIdOrNull(name.value.value)
        handle[NAME] = name.value.copy(errorId = nameErrorId)
        if (nameErrorId != null) return
        val uuid = existingPillUuid.value ?: UUID.randomUUID().toString()
        pillsDatabaseNodeReference
            .child(uuid)
            .setValue(
                PillDescription(
                    uuid = uuid,
                    name = name.value.value,
                    dosageType = selectedDosageType.value,
                    dosage = dosage.value.value.toFloat(),
                    notes = notes.value.value,
                    remaindersDates = reminderDates.value.encodeToStringsCollection().toList(),
                )
            )
        events.trySend(PillEditEvents.NavigateBack)
    }

    fun onSaveChangesDialogDismiss() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = false
    }

    fun onRemindDatePicked(date: LocalDateTime) {
        val stringsSet = (reminderDates.value + date).encodeToStringsCollection().toSet()
        handle[REMINDER_DATES] = stringsSet
    }

    fun onRemindDateRemoved(date: LocalDateTime) {
        val stringsSet = (reminderDates.value - date).encodeToStringsCollection().toSet()
        handle[REMINDER_DATES] = stringsSet
    }
}