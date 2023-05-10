package com.example.simplemedicplan.feature.home.pills

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.application.theme.DarkRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.utils.FAB_SIZE
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun PillsScreen(
    viewModel: PillsViewModelMock = hiltViewModel(),
    onNavigateToAddPill: () -> Unit
) {

    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val fabPadding = 16
    val bottomContentPadding = remember { fabPadding + FAB_SIZE }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is PillsEvents.NavigateToAddPill -> onNavigateToAddPill()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = WindowInsets.statusBars.add(
                WindowInsets(bottom = bottomContentPadding.dp)
            ).asPaddingValues(),
        ) {

        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(fabPadding.dp),
            onClick = viewModel::onAddButtonClick,
            containerColor = YellowColor,
            contentColor = DarkRedColor
        ) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }
}

@Preview
@Composable
private fun PillsScreenPreview() {
    PillsScreen(viewModel = PillsViewModelMockImpl(), onNavigateToAddPill = {})
}