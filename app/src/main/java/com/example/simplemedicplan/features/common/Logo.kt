package com.example.simplemedicplan.features.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.simplemedicplan.ui.theme.DarkRedColor
import com.example.simplemedicplan.ui.theme.LightRedColor
import com.example.simplemedicplan.ui.theme.YellowColor

@Composable
fun LogoMedium(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(200.dp)
            .background(
                brush = remember {
                    Brush.radialGradient(
                        colors = listOf(
                            YellowColor,
                            YellowColor,
                            YellowColor,
                            LightRedColor,
                            DarkRedColor
                        )
                    )
                },
                shape = CircleShape
            )
            .padding(horizontal = 50.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = buildAnnotatedString {
                pushStyle(ParagraphStyle(textAlign = TextAlign.Left))
                append("Simple")
                pop()
                pushStyle(ParagraphStyle(textAlign = TextAlign.Center))
                append("Medic")
                pop()
                pushStyle(ParagraphStyle(textAlign = TextAlign.Right))
                append("Plan")
            },
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )
    }
}