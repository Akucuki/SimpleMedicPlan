package com.example.simplemedicplan.feature.home.pills.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemedicplan.model.TextFieldValueWrapper
import com.example.simplemedicplan.model.home.PillDescription
import com.example.simplemedicplan.model.home.PillDosageType
import com.example.simplemedicplan.utils.FIREBASE_DATABASE_PILLS_DESCRIPTION
import com.example.simplemedicplan.utils.FIREBASE_DATABASE_URL
import com.example.simplemedicplan.utils.FIREBASE_DATABASE_USERS
import com.example.simplemedicplan.utils.InputValidator
import com.example.simplemedicplan.utils.decodeToLocalDateTimeCollection
import com.example.simplemedicplan.utils.encodeToStringsCollection
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

private const val NAME = "name"

//private const val FORM_TYPE = "form_type"
//private const val IS_FORM_DROPDOWN_EXPANDED = "is_form_dropdown_expanded"
private const val DOSAGE_TYPE = "dosage_type"
private const val IS_DOSAGE_DROPDOWN_EXPANDED = "is_dosage_dropdown_expanded"
private const val DOSAGE = "dosage"

//private const val FREQUENCY = "frequency"
//private const val INTAKE_TIME = "intake_time"
//private const val COURSE_DURATION = "course_duration"
private const val NOTES = "notes"
private const val IS_REMINDER_ENABLED = "reminder_enabled"

//private const val REMINDER_TIME = "reminder_time"
private const val IS_SAVE_CHANGES_DIALOG_VISIBLE = "is_save_changes_dialog_visible"
private const val REMINDER_DATES = "reminder_dates"

@HiltViewModel
class PillEditViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    databaseReference: DatabaseReference
) : ViewModel() {

    val events = Channel<PillEditEvents>(Channel.UNLIMITED)

    // Fields values
    val name = handle.getStateFlow(NAME, TextFieldValueWrapper())
    val dosage = handle.getStateFlow(DOSAGE, TextFieldValueWrapper(value = "1.0"))

    //    val frequency = handle.getStateFlow(FREQUENCY, TextFieldValueWrapper())
//    val intakeTime = handle.getStateFlow(INTAKE_TIME, TextFieldValueWrapper())
//    val courseDuration = handle.getStateFlow(COURSE_DURATION, TextFieldValueWrapper())
    val notes = handle.getStateFlow(NOTES, TextFieldValueWrapper())
    val isReminderEnabled = handle.getStateFlow(IS_REMINDER_ENABLED, false)
//    val reminderTime = handle.getStateFlow(REMINDER_TIME, TextFieldValueWrapper())

    // Dropdowns values
//    val selectedFormType = handle.getStateFlow(FORM_TYPE, PillFormType.TABLETS)
    val selectedDosageType = handle.getStateFlow(DOSAGE_TYPE, PillDosageType.MILLIGRAMS)

    // Visibility values
//    val isFormDropdownExpanded = handle.getStateFlow(IS_FORM_DROPDOWN_EXPANDED, false)
    val isDosageDropdownExpanded = handle.getStateFlow(IS_DOSAGE_DROPDOWN_EXPANDED, false)
    val isSaveChangesDialogVisible = handle.getStateFlow(IS_SAVE_CHANGES_DIALOG_VISIBLE, false)

    // Dates management
    val reminderDates =
        handle.getStateFlow(REMINDER_DATES, setOf<String>()).map { stringEncodedDates ->
            stringEncodedDates.decodeToLocalDateTimeCollection().toSet()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptySet())

    private val pillsDatabaseNodeReference = databaseReference
        .child(FIREBASE_DATABASE_PILLS_DESCRIPTION)

    fun onNameChange(input: String) {
        handle[NAME] = name.value.copy(value = input, errorId = null)
    }

//    fun onFormTypeSelect(formType: PillFormType) {
//        handle[FORM_TYPE] = formType
//        onFormDropdownDismiss()
//    }

//    fun onFormDropdownExpandClick() {
//        handle[IS_FORM_DROPDOWN_EXPANDED] = !isFormDropdownExpanded.value
//    }

    fun onDosageDropdownExpandClick() {
        handle[IS_DOSAGE_DROPDOWN_EXPANDED] = true
    }

//    fun onFormDropdownDismiss() {
//        handle[IS_FORM_DROPDOWN_EXPANDED] = false
//    }

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

//    fun onFrequencyChange(input: String) {
//        handle[FREQUENCY] = frequency.value.copy(value = input)
//    }
//
//    fun onIntakeTimeChange(input: String) {
//        handle[INTAKE_TIME] = intakeTime.value.copy(value = input)
//    }
//
//    fun onCourseDurationChange(input: String) {
//        handle[COURSE_DURATION] = courseDuration.value.copy(value = input)
//    }

    fun onNotesChange(input: String) {
        handle[NOTES] = notes.value.copy(value = input)
    }

    fun onIsReminderEnabledChange(isEnabled: Boolean) {
        handle[IS_REMINDER_ENABLED] = isEnabled
    }

//    fun onReminderTimeChange(input: String) {
//        handle[REMINDER_TIME] = reminderTime.value.copy(value = input)
//    }

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
        val uuid = UUID.randomUUID().toString()
        pillsDatabaseNodeReference
            .child(uuid)
            .setValue(
                PillDescription(
                    uuid = uuid,
                    name = name.value.value,
                    dosageType = selectedDosageType.value,
                    dosage = dosage.value.value.toFloat(),
                    notes = notes.value.value,
//                endDate = LocalDate.now().toEpochDay(),
                    remaindersDates = emptyList(),
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