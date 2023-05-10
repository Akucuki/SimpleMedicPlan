package com.example.simplemedicplan.feature.home.pills.edit

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.simplemedicplan.R
import com.example.simplemedicplan.feature.common.SMpAppBar

@Composable
fun PillEditScreen(
    onSaveClick: () -> Unit = {},
    onNavigateBack: () -> Unit = {},
) {
    SMpAppBar(
        title = stringResource(R.string.edit_medicine),
        leadingIcon = Icons.Filled.ArrowBack,
        onLeadingIconClick = {},
    )
}