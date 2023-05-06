package com.example.simplemedicplan.feature.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.simplemedicplan.application.theme.DarkRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.utils.APP_BAR_HEIGHT

@Composable
fun SMpAppBar(
    modifier: Modifier = Modifier,
    title: String,
    leadingIcon: ImageVector? = null,
    onLeadingIconClick: (() -> Unit)? = null,
    trailingIcon: ImageVector? = null,
    onTrailingIconClick: (() -> Unit)? = null
) {
    Column {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .background(color = YellowColor)
                .statusBarsPadding()
                .height(APP_BAR_HEIGHT.dp)
        ) {
            if (leadingIcon != null && onLeadingIconClick != null) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterStart),
                    onClick = onLeadingIconClick
                ) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = DarkRedColor
                    )
                }
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = DarkRedColor
            )
            if (trailingIcon != null && onTrailingIconClick != null) {
                IconButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = onTrailingIconClick
                ) {
                    Icon(
                        imageVector = trailingIcon,
                        contentDescription = null,
                        tint = DarkRedColor
                    )
                }
            }
        }
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = remember {
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = .1f),
                            Color.Transparent
                        )
                    )
                }
            )
            .height(4.dp))
    }
}