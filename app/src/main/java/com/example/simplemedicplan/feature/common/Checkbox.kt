package com.example.simplemedicplan.feature.common

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.simplemedicplan.application.theme.DarkBlueColor
import com.example.simplemedicplan.application.theme.YellowColor

@Composable
fun PrimaryCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Checkbox(
        modifier = modifier,
        checked = checked,
        onCheckedChange = onCheckedChange,
        colors = checkboxColors()
    )
}

@Composable
private fun checkboxColors() = CheckboxDefaults.colors(
    checkedColor = YellowColor,
    uncheckedColor = YellowColor,
    checkmarkColor = DarkBlueColor
)