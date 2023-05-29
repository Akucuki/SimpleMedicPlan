package com.example.simplemedicplan.feature.home.pills.edit

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.LightRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.feature.common.DatesListEditor
import com.example.simplemedicplan.feature.common.DropdownTriggerButton
import com.example.simplemedicplan.feature.common.PrimaryButton
import com.example.simplemedicplan.feature.common.PrimaryCheckbox
import com.example.simplemedicplan.feature.common.PrimaryDropdownMenu
import com.example.simplemedicplan.feature.common.PrimaryDropdownMenuItem
import com.example.simplemedicplan.feature.common.SMpAppBar
import com.example.simplemedicplan.feature.common.SaveChangesDialog
import com.example.simplemedicplan.feature.common.SecondaryTextField
import com.example.simplemedicplan.model.home.PillDescription
import com.example.simplemedicplan.model.home.PillDosageType
import com.example.simplemedicplan.utils.APP_BAR_HEIGHT
import com.example.simplemedicplan.utils.alarm.AlarmBroadcastReceiver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDateTime
import java.time.ZoneOffset

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PillEditScreen(
    viewModel: PillEditViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    pillUuid: String?
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val name by viewModel.name.collectAsStateWithLifecycle()
    val selectedDosageType by viewModel.selectedDosageType.collectAsStateWithLifecycle()
    val isDosageDropdownExpanded by viewModel.isDosageDropdownExpanded.collectAsStateWithLifecycle()
    val dosage by viewModel.dosage.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val isReminderEnabled by viewModel.isReminderEnabled.collectAsStateWithLifecycle()

    val isSaveChangesDialogVisible by viewModel.isSaveChangesDialogVisible.collectAsStateWithLifecycle()

    val reminderDates by viewModel.reminderDates.collectAsStateWithLifecycle()

    val postNotificationsPermissionState = if (Build.VERSION.SDK_INT >= 33) {
        rememberPermissionState(
            Manifest.permission.POST_NOTIFICATIONS
        )
    } else {
        null
    }

    BackHandler(onBack = viewModel::onBackClick)

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is PillEditEvents.NavigateBack -> onNavigateBack()
                is PillEditEvents.DismissReminder -> {
                    dismissReminder(
                        context,
                        event.date,
                        event.notificationTag,
                        event.pillName,
                        PillDescription.composeNotificationText(
                            context,
                            event.pillName,
                            event.pillDosage,
                            event.pillDosageType
                        )
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        pillUuid?.let { viewModel.onExistingPillUuidObtained(it) }
    }

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
                .padding(vertical = 8.dp)
        ) {
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = name,
                onValueChange = viewModel::onNameChange,
                labelText = stringResource(R.string.name),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )
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
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(.5f)
                ) {
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            SecondaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = notes,
                onValueChange = viewModel::onNotesChange,
                labelText = stringResource(R.string.notes),
                singleLine = false,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .align(Alignment.End),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (postNotificationsPermissionState?.status?.isGranted != false) {
                        DatesListEditor(
                            dates = reminderDates,
                            onDatePicked = viewModel::onRemindDatePicked,
                            onDateRemoveClick = viewModel::onRemindDateRemoved
                        )
                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.error_alarm_permission_is_not_granted),
                                style = MaterialTheme.typography.bodyMedium,
                                color = LightRedColor
                            )
                            PrimaryButton(
                                text = stringResource(R.string.grant_permission),
                                onClick = {
                                    postNotificationsPermissionState.launchPermissionRequest()
                                }
                            )
                        }
                    }
                }
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

private fun dismissReminder(
    context: Context,
    dateTime: LocalDateTime,
    notificationTag: String,
    notificationName: String,
    notificationText: String
) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val intent = Intent(context, AlarmBroadcastReceiver::class.java).apply {
        putExtra(AlarmBroadcastReceiver.NOTIFICATION_TAG, notificationTag)
        putExtra(AlarmBroadcastReceiver.NOTIFICATION_NAME, notificationName)
        putExtra(AlarmBroadcastReceiver.NOTIFICATION_TEXT, notificationText)
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        dateTime.toEpochSecond(ZoneOffset.UTC).toInt(),
        intent,
        PendingIntent.FLAG_IMMUTABLE
    )
    if (
        ContextCompat.checkSelfPermission(context, Manifest.permission.SCHEDULE_EXACT_ALARM)
        == PackageManager.PERMISSION_GRANTED
    ) {
        Log.d("vitalik", "Dismissing an alarm with tag $notificationTag")
        alarmManager.cancel(pendingIntent)
    }
}