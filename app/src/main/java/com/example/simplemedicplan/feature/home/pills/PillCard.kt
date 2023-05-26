package com.example.simplemedicplan.feature.home.pills

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.model.home.PillDescriptionUI
import com.example.simplemedicplan.model.home.PillDosageType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PillCard(
    modifier: Modifier = Modifier,
    pillDescriptionUI: PillDescriptionUI,
    onClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(width = 2.dp, color = YellowColor),
        onClick = onClick,
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = pillDescriptionUI.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = pillDescriptionUI.dosage.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(pillDescriptionUI.dosageType.labelId),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = stringResource(R.string.till),
                    style = MaterialTheme.typography.titleMedium
                )
//                Text(
//                    text = pillDescriptionUI.endDate,
//                    style = MaterialTheme.typography.titleMedium
//                )
            }
        }
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