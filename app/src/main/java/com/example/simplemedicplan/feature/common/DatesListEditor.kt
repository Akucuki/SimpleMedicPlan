package com.example.simplemedicplan.feature.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.LightRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM HH:mm")

@Composable
fun DatesListEditor(
    modifier: Modifier = Modifier,
    dates: Set<LocalDateTime>,
    onDatePicked: (LocalDateTime) -> Unit,
    onDateRemoveClick: (LocalDateTime) -> Unit
) {
    val context = LocalContext.current
    var datePicked by remember { mutableStateOf<LocalDate?>(null) }
    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val time = LocalTime.of(hourOfDay, minute)
                val timeAfter10Minutes = LocalTime.now()// TODO turn back after testing .plusMinutes(10)
                if (time.isBefore(timeAfter10Minutes)) {
                    Toast.makeText(
                        context,
                        context.getString(R.string.error_reminder_time_cannot_be_in_the_past),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    onDatePicked(datePicked!!.atTime(time))
                }
            },
            LocalDateTime.now().hour,
            LocalDateTime.now().minute,
            true
        )
    }
    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                datePicked = LocalDate.of(year, month, dayOfMonth)
                timePickerDialog.show()
            },
            LocalDateTime.now().year,
            LocalDateTime.now().monthValue,
            LocalDateTime.now().dayOfMonth
        ).apply {
            datePicker.minDate = System.currentTimeMillis() - 1000
        }
    }
    val datesList = remember(dates) { dates.toList().sortedBy { it } }

    Column(
        modifier = modifier.border(
            width = 2.dp,
            color = YellowColor,
            shape = remember { RoundedCornerShape(10.dp) })
    ) {
        datesList.forEach { date ->
            DateRow(
                modifier = Modifier.fillMaxWidth(),
                text = dateTimeFormatter.format(date),
                onDateClick = {},
                onRemoveClick = { onDateRemoveClick(date) }
            )
        }
        AddButton(modifier = Modifier.fillMaxWidth(), onClick = { datePickerDialog.show() })
    }
}

@Composable
private fun DateRow(
    modifier: Modifier = Modifier,
    text: String,
    onDateClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onRemoveClick) {
            Icon(
                painter = painterResource(R.drawable.ic_delete_forever),
                tint = LightRedColor,
                contentDescription = null
            )
        }
        TextButton(modifier = Modifier.fillMaxWidth(), onClick = onDateClick) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = YellowColor
            )
        }
    }
}

@Composable
private fun AddButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(modifier = modifier, onClick = onClick) {
        Icon(imageVector = Icons.Filled.Add, tint = YellowColor, contentDescription = null)
    }
}