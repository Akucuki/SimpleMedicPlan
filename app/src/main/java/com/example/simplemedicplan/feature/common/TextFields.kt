package com.example.simplemedicplan.feature.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.simplemedicplan.application.theme.LightRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.model.TextFieldValueWrapper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrimaryTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValueWrapper,
    onValueChange: (String) -> Unit,
    labelText: String
) {
    val isError = value.errorId != null
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = value.value,
            onValueChange = onValueChange,
            colors = textFieldColors(),
            isError = isError,
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true,
            label = {
                Text(
                    text = labelText,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isError) LightRedColor else YellowColor
                )
            },
        )
        val errorLineModifier = Modifier.height(14.dp)
        if (isError) {
            Text(
                modifier = errorLineModifier,
                text = stringResource(value.errorId!!),
                style = MaterialTheme.typography.bodySmall,
                color = LightRedColor
            )
        } else {
            Spacer(errorLineModifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondaryTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValueWrapper,
    onValueChange: (String) -> Unit,
    labelText: String,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    val isError = value.errorId != null
    Column(modifier = modifier, horizontalAlignment = Alignment.End) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier.weight(.5f),
                text = labelText,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isError) LightRedColor else YellowColor
            )
            TextField(
                modifier = Modifier.weight(.5f),
                value = value.value,
                onValueChange = onValueChange,
                colors = textFieldColors(),
                isError = isError,
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = singleLine,
                keyboardOptions = keyboardOptions
            )
        }
        val errorLineModifier = Modifier.height(14.dp)
        if (isError) {
            Text(
                modifier = errorLineModifier,
                text = stringResource(value.errorId!!),
                style = MaterialTheme.typography.bodySmall,
                color = LightRedColor
            )
        } else {
            Spacer(errorLineModifier)
        }
    }
}

@Composable
fun PrimaryDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest,
        content = content
    )
}

@Composable
fun PrimaryDropdownMenuItem(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        modifier = modifier,
        text = {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        },
        onClick = onClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun textFieldColors() = TextFieldDefaults.textFieldColors(
    cursorColor = Color.White,
    focusedLabelColor = Color.White,
    unfocusedLabelColor = YellowColor,
    focusedIndicatorColor = Color.White,
    unfocusedIndicatorColor = YellowColor,
    focusedLeadingIconColor = Color.White,
    unfocusedLeadingIconColor = YellowColor,
    focusedTrailingIconColor = Color.White,
    unfocusedTrailingIconColor = YellowColor,
    textColor = Color.White,
    errorCursorColor = LightRedColor,
    errorLabelColor = LightRedColor,
    errorIndicatorColor = LightRedColor,
    errorLeadingIconColor = LightRedColor,
    errorTrailingIconColor = LightRedColor,
    containerColor = Color.Transparent
)