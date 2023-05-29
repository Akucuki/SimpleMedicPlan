package com.example.simplemedicplan.feature.home.pills

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.DarkRedColor
import com.example.simplemedicplan.application.theme.LightRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.feature.common.InfiniteProgressIndicator
import com.example.simplemedicplan.model.home.PillDescription
import com.example.simplemedicplan.utils.FAB_SIZE
import com.example.simplemedicplan.utils.alarm.AlarmBroadcastReceiver
import kotlinx.coroutines.flow.receiveAsFlow
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar

@Composable
fun PillsScreen(
    viewModel: PillsViewModel = hiltViewModel(),
    onNavigateToEditPill: (String?) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val fabPadding = 16
    val bottomContentPadding = remember { fabPadding + FAB_SIZE }
    val pillsDescriptions by viewModel.pillsDescriptions.collectAsStateWithLifecycle()
    val isLoadingInProgress by viewModel.isLoadingInProgress.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is PillsEvents.NavigateToEditPill -> onNavigateToEditPill(event.pillDescriptionUuid)
                is PillsEvents.RegisterReminder -> {
                    registerReminderNotificationAlarm(
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
                is PillsEvents.DismissReminder -> {
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

    LaunchedEffect(Unit) { viewModel.fetchPillsDescriptions() }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = WindowInsets.statusBars.add(
                WindowInsets(
                    left = 16.dp,
                    right = 16.dp,
                    top = 8.dp,
                    bottom = bottomContentPadding.dp
                )
            ).asPaddingValues(),
        ) {
            items(pillsDescriptions) { pillDescription ->
                PillCard(
                    modifier = Modifier.padding(bottom = 4.dp),
                    pillDescriptionUI = pillDescription,
                    onClick = { viewModel.onPillCardClick(pillDescription) },
                    onRemoveClick = { viewModel.onPillCardRemoveClick(pillDescription) },
                    onEditClick = { viewModel.onPillCardEditClick(pillDescription.uuid) }
                )
            }
        }
        AnimatedVisibility(visible = isLoadingInProgress) {
            InfiniteProgressIndicator(
                modifier = Modifier.padding(bottom = bottomContentPadding.dp)
            )
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = pillsDescriptions.isEmpty() && !isLoadingInProgress
        ) {
            Text(
                text = stringResource(R.string.there_are_no_medicines),
                style = MaterialTheme.typography.titleMedium,
                color = LightRedColor
            )
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(fabPadding.dp),
            onClick = viewModel::onAddButtonClick,
            containerColor = YellowColor,
            contentColor = DarkRedColor
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}

private fun registerReminderNotificationAlarm(
    context: Context,
    dateTime: LocalDateTime,
    notificationTag: String,
    notificationName: String,
    notificationText: String,
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
    val alarmTime = Calendar.getInstance().apply {
        set(Calendar.YEAR, dateTime.year)
        set(Calendar.MONTH, dateTime.monthValue - 1)
        set(Calendar.DAY_OF_MONTH, dateTime.dayOfMonth)
        set(Calendar.HOUR_OF_DAY, dateTime.hour)
        set(Calendar.MINUTE, dateTime.minute)
    }.timeInMillis
    if (System.currentTimeMillis() > alarmTime) {
        Log.d("vitalik", "Tried scheduling an alarm with tag $notificationTag, but it's in the past")
        return
    }
    if (
        ContextCompat.checkSelfPermission(context, Manifest.permission.SCHEDULE_EXACT_ALARM)
        == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.S
    ) {
        Log.d("vitalik", "Scheduling an alarm with tag $notificationTag")
        val alarmClockInfo = AlarmManager.AlarmClockInfo(alarmTime, pendingIntent)
        alarmManager.setAlarmClock(alarmClockInfo, pendingIntent)
    } else {
        Toast.makeText(
            context,
            R.string.error_alarm_permission_is_not_granted,
            Toast.LENGTH_SHORT
        ).show()
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

// TODO fix this to correctly work with Hilt
//@Preview
//@Composable
//private fun PillsScreenPreview() {
//    PillsScreen(viewModel = PillsViewModelMockImpl(), onNavigateToAddPill = {})
//}