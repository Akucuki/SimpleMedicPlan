package com.example.simplemedicplan.feature.home.pills.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.simplemedicplan.model.TextFieldValueWrapper
import com.example.simplemedicplan.model.home.PillDosageType
import javax.inject.Inject

private const val NAME = "name"
//private const val FORM_TYPE = "form_type"
private const val IS_FORM_DROPDOWN_EXPANDED = "is_form_dropdown_expanded"
private const val DOSAGE_TYPE = "dosage_type"
private const val IS_DOSAGE_DROPDOWN_EXPANDED = "is_dosage_dropdown_expanded"
private const val DOSAGE = "dosage"
private const val FREQUENCY = "frequency"
private const val INTAKE_TIME = "intake_time"
private const val COURSE_DURATION = "course_duration"
private const val NOTES = "notes"
private const val IS_REMINDER_ENABLED = "reminder_enabled"
private const val REMINDER_TIME = "reminder_time"
private const val IS_SAVE_CHANGES_DIALOG_VISIBLE = "is_save_changes_dialog_visible"

class PillEditViewModel @Inject constructor(
    private val handle: SavedStateHandle,
) : ViewModel() {

    // Fields values
    val name = handle.getStateFlow(NAME, TextFieldValueWrapper())
    val dosage = handle.getStateFlow(DOSAGE, TextFieldValueWrapper())
    val frequency = handle.getStateFlow(FREQUENCY, TextFieldValueWrapper())
    val intakeTime = handle.getStateFlow(INTAKE_TIME, TextFieldValueWrapper())
    val courseDuration = handle.getStateFlow(COURSE_DURATION, TextFieldValueWrapper())
    val notes = handle.getStateFlow(NOTES, TextFieldValueWrapper())
    val isReminderEnabled = handle.getStateFlow(IS_REMINDER_ENABLED, false)
    val reminderTime = handle.getStateFlow(REMINDER_TIME, TextFieldValueWrapper())
    // Dropdowns values
//    val selectedFormType = handle.getStateFlow(FORM_TYPE, PillFormType.TABLETS)
    val selectedDosageType = handle.getStateFlow(DOSAGE_TYPE, PillDosageType.MILLIGRAMS)
    // Visibility values
    val isFormDropdownExpanded = handle.getStateFlow(IS_FORM_DROPDOWN_EXPANDED, false)
    val isDosageDropdownExpanded = handle.getStateFlow(IS_DOSAGE_DROPDOWN_EXPANDED, false)
    val isSaveChangesDialogVisible = handle.getStateFlow(IS_SAVE_CHANGES_DIALOG_VISIBLE, false)

    fun onNameChange(input: String) {
        handle[NAME] = name.value.copy(value = input)
    }

//    fun onFormTypeSelect(formType: PillFormType) {
//        handle[FORM_TYPE] = formType
//        onFormDropdownDismiss()
//    }

    fun onFormDropdownExpandClick() {
        handle[IS_FORM_DROPDOWN_EXPANDED] = !isFormDropdownExpanded.value
    }

    fun onDosageDropdownExpandClick() {
        handle[IS_DOSAGE_DROPDOWN_EXPANDED] = !isFormDropdownExpanded.value
    }

    fun onFormDropdownDismiss() {
        handle[IS_FORM_DROPDOWN_EXPANDED] = false
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

    fun onFrequencyChange(input: String) {
        handle[FREQUENCY] = frequency.value.copy(value = input)
    }

    fun onIntakeTimeChange(input: String) {
        handle[INTAKE_TIME] = intakeTime.value.copy(value = input)
    }

    fun onCourseDurationChange(input: String) {
        handle[COURSE_DURATION] = courseDuration.value.copy(value = input)
    }

    fun onNotesChange(input: String) {
        handle[NOTES] = notes.value.copy(value = input)
    }

    fun onIsReminderEnabledChange(isEnabled: Boolean) {
        handle[IS_REMINDER_ENABLED] = isEnabled
    }

    fun onReminderTimeChange(input: String) {
        handle[REMINDER_TIME] = reminderTime.value.copy(value = input)
    }

    fun onBackClick() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = true
    }

    fun onSaveChangesDialogConfirmClick() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = false
        // TODO navigate back
    }

    fun onSaveChangesDialogDiscardClick() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = false
        // TODO navigate back
    }

    fun onSaveClick() {

    }

    fun onSaveChangesDialogDismiss() {
        handle[IS_SAVE_CHANGES_DIALOG_VISIBLE] = false
    }
}