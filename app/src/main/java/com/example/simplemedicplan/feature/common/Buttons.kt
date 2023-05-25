package com.example.simplemedicplan.feature.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.example.simplemedicplan.application.theme.DarkBlueColor
import com.example.simplemedicplan.application.theme.DarkRedColor
import com.example.simplemedicplan.application.theme.YellowColor

@Composable
fun PrimaryButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = primaryButtonColors(),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun SecondaryButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    OutlinedButton(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = secondaryButtonColors(),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
        border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(YellowColor)),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun DropdownTriggerButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    OutlinedButton(
        modifier = modifier,
        onClick = onClick,
        content = content,
        border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(YellowColor)),
        colors = dropdownTriggerButtonColors(),
    )
}

@Composable
private fun primaryButtonColors() = ButtonDefaults.buttonColors(
    containerColor = DarkRedColor,
    contentColor = YellowColor
)

@Composable
private fun secondaryButtonColors() = ButtonDefaults.outlinedButtonColors(
    contentColor = YellowColor,
    containerColor = DarkBlueColor
)

@Composable
private fun dropdownTriggerButtonColors() = ButtonDefaults.outlinedButtonColors(
    contentColor = Color.White,
    containerColor = DarkBlueColor,
)