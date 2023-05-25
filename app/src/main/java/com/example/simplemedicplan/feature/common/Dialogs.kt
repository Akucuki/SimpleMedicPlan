package com.example.simplemedicplan.feature.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.YellowColor

@Composable
fun SaveChangesDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSaveClik: () -> Unit,
    onDiscardChangesClick: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        confirmButton = {
            SecondaryButton(text = stringResource(R.string.save), onClick = onSaveClik)
        },
        dismissButton = {
            PrimaryButton(
                text = stringResource(R.string.discard_changes),
                onClick = onDiscardChangesClick
            )
        },
        title = {
            Text(
                text = stringResource(R.string.save_changes_dialog_title),
                style = MaterialTheme.typography.titleMedium,
                color = YellowColor
            )
        },
        text = {
            Text(
                text = stringResource(R.string.save_changes_dialog_text),
                style = MaterialTheme.typography.bodyMedium,
                color = YellowColor
            )
        }
    )
}