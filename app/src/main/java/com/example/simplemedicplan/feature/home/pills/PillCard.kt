package com.example.simplemedicplan.feature.home.pills

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.LightRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.model.home.PillDescriptionUI
import com.example.simplemedicplan.model.home.PillDosageType
import java.util.UUID

@Composable
fun PillCard(
    modifier: Modifier = Modifier,
    pillDescriptionUI: PillDescriptionUI,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
    onRemoveClick: () -> Unit = {},
) {
//    Card(
//        modifier = modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .border(width = 2.dp, color = YellowColor),
//        onClick = onClick,
//    ) {
    val roundedCornerShape = remember { RoundedCornerShape(8.dp) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(roundedCornerShape)
            .clickable(onClick = onClick)
            .border(width = 2.dp, color = YellowColor, shape = roundedCornerShape)
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = pillDescriptionUI.name,
                style = MaterialTheme.typography.titleLarge,
                color = YellowColor
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Filled.Edit, tint = YellowColor, contentDescription = null)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = pillDescriptionUI.dosage.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = YellowColor
            )
            Text(
                text = stringResource(pillDescriptionUI.dosageType.labelId),
                style = MaterialTheme.typography.titleMedium,
                color = YellowColor
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = onRemoveClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete_forever),
                    tint = LightRedColor,
                    contentDescription = null
                )
            }
//                Text(
//                    text = pillDescriptionUI.endDate,
//                    style = MaterialTheme.typography.titleMedium
//                )
        }
        AnimatedVisibility(visible = pillDescriptionUI.isExpanded) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    modifier = Modifier.align(Alignment.Start),
                    text = stringResource(R.string.notes),
                    style = MaterialTheme.typography.titleMedium,
                    color = YellowColor
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = pillDescriptionUI.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = YellowColor
                )
            }
        }
//        }
    }
}

@Preview(
    device = Devices.PIXEL_4,
    showSystemUi = true,
    showBackground = true,
    backgroundColor = 0xFF3891A6
)
@Composable
private fun PillCardPreview() {
    PillCard(
        pillDescriptionUI = PillDescriptionUI(
            uuid = "",
            name = "Lily Noble",
//            formType = PillFormType.CAPSULES,
            dosageType = PillDosageType.CAPSULES,
            dosage = 1f,
//            endDate = "12 May 2023",
            notes = "For a sour aromatic sauce, add some olive oil and jasmine.",
            isExpanded = false
        )
    )
}