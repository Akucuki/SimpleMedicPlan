package com.example.simplemedicplan.feature.home.pills.edit

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.feature.common.DropdownTriggerButton
import com.example.simplemedicplan.feature.common.PrimaryCheckbox
import com.example.simplemedicplan.feature.common.PrimaryDropdownMenu
import com.example.simplemedicplan.feature.common.PrimaryDropdownMenuItem
import com.example.simplemedicplan.feature.common.SMpAppBar
import com.example.simplemedicplan.feature.common.SaveChangesDialog
import com.example.simplemedicplan.feature.common.SecondaryTextField
import com.example.simplemedicplan.model.home.PillDosageType
import com.example.simplemedicplan.utils.APP_BAR_HEIGHT

@Composable
fun PillEditScreen(
    viewModel: PillEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {

    val name by viewModel.name.collectAsStateWithLifecycle()
//    val selectedFormType by viewModel.selectedFormType.collectAsStateWithLifecycle()
    val isFormDropdownExpanded by viewModel.isFormDropdownExpanded.collectAsStateWithLifecycle()
    val selectedDosageType by viewModel.selectedDosageType.collectAsStateWithLifecycle()
    val isDosageDropdownExpanded by viewModel.isDosageDropdownExpanded.collectAsStateWithLifecycle()
    val dosage by viewModel.dosage.collectAsStateWithLifecycle()
    val frequency by viewModel.frequency.collectAsStateWithLifecycle()
    val intakeTime by viewModel.intakeTime.collectAsStateWithLifecycle()
    val courseDuration by viewModel.courseDuration.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val isReminderEnabled by viewModel.isReminderEnabled.collectAsStateWithLifecycle()
    val reminderTime by viewModel.reminderTime.collectAsStateWithLifecycle()

    val isSaveChangesDialogVisible by viewModel.isSaveChangesDialogVisible.collectAsStateWithLifecycle()

    BackHandler(onBack = viewModel::onBackClick)

    Box(modifier = Modifier.fillMaxSize()) {
        SMpAppBar(
            title = stringResource(R.string.edit_medicine),
            trailingIcon = ImageVector.vectorResource(id = R.drawable.ic_save),
            onTrailingIconClick = viewModel::onSaveClick,
        )
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = APP_BAR_HEIGHT.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = viewModel::onNameChange,
                labelText = stringResource(R.string.name),
            )
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Text(
//                    modifier = Modifier.weight(.5f),
//                    text = stringResource(R.string.form),
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = YellowColor
//                )
//                Box(modifier = Modifier.weight(.5f)) {
//                    DropdownTriggerButton(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = viewModel::onFormDropdownExpandClick
//                    ) {
//                        val iconId = if (isFormDropdownExpanded) {
//                            R.drawable.ic_arrow_drop_up
//                        } else {
//                            R.drawable.ic_arrow_drop_down
//                        }
//                        Text(
//                            modifier = Modifier.weight(1f),
//                            text = stringResource(selectedFormType.labelId),
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = Color.White
//                        )
//                        Icon(
//                            painter = painterResource(iconId),
//                            tint = YellowColor,
//                            contentDescription = null
//                        )
//                    }
//                    PrimaryDropdownMenu(
//                        modifier = Modifier.heightIn(max = 200.dp),
//                        expanded = isFormDropdownExpanded,
//                        onDismissRequest = viewModel::onFormDropdownDismiss
//                    ) {
//                        PillFormType.values().forEach { form ->
//                            PrimaryDropdownMenuItem(
//                                text = stringResource(form.labelId),
//                                onClick = { viewModel.onFormTypeSelect(form) }
//                            )
//                        }
//                    }
//                }
//            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(.5f),
                    text = stringResource(R.string.dosage_type),
                    style = MaterialTheme.typography.bodyMedium,
                    color = YellowColor
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(.5f)) {
                    DropdownTriggerButton(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = viewModel::onDosageDropdownExpandClick
                    ) {
                        val iconId = if (isDosageDropdownExpanded) {
                            R.drawable.ic_arrow_drop_up
                        } else {
                            R.drawable.ic_arrow_drop_down
                        }
                        Text(
                            modifier = Modifier.weight(1f),
                            text = stringResource(selectedDosageType.labelId),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Icon(
                            painter = painterResource(iconId),
                            tint = YellowColor,
                            contentDescription = null
                        )
                    }
                    PrimaryDropdownMenu(
                        modifier = Modifier.heightIn(max = 200.dp),
                        expanded = isDosageDropdownExpanded,
                        onDismissRequest = viewModel::onDosageDropdownDismiss
                    ) {
                        PillDosageType.values().forEach { dosageType ->
                            PrimaryDropdownMenuItem(
                                text = stringResource(dosageType.labelId),
                                onClick = { viewModel.onDosageTypeSelect(dosageType) }
                            )
                        }
                    }
                }
            }
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = dosage,
                onValueChange = viewModel::onDosageChange,
                labelText = stringResource(R.string.dosage),
            )
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = frequency,
                onValueChange = viewModel::onFrequencyChange,
                labelText = stringResource(R.string.frequency),
            )
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = intakeTime,
                onValueChange = viewModel::onIntakeTimeChange,
                labelText = stringResource(R.string.intake_time),
            )
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = courseDuration,
                onValueChange = viewModel::onCourseDurationChange,
                labelText = stringResource(R.string.course_duration),
            )
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = notes,
                onValueChange = viewModel::onNotesChange,
                labelText = stringResource(R.string.notes),
                singleLine = false
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(.5f),
                    text = stringResource(R.string.remind_me),
                    style = MaterialTheme.typography.bodyMedium,
                    color = YellowColor
                )
                PrimaryCheckbox(
                    checked = isReminderEnabled,
                    onCheckedChange = viewModel::onIsReminderEnabledChange
                )
            }
            if (isReminderEnabled) {
                SecondaryTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = reminderTime,
                    onValueChange = viewModel::onReminderTimeChange,
                    labelText = stringResource(R.string.time),
                )
            }
        }
        if (isSaveChangesDialogVisible) {
            SaveChangesDialog(
                onDismissRequest = viewModel::onSaveChangesDialogDismiss,
                onSaveClick = viewModel::onSaveChangesDialogConfirmClick,
                onDiscardChangesClick = viewModel::onSaveChangesDialogDiscardClick
            )
        }
    }
}