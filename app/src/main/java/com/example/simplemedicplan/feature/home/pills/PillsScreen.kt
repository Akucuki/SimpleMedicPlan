package com.example.simplemedicplan.feature.home.pills

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.DarkRedColor
import com.example.simplemedicplan.application.theme.LightRedColor
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.feature.common.InfiniteProgressIndicator
import com.example.simplemedicplan.utils.FAB_SIZE
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun PillsScreen(
    viewModel: PillsViewModel = hiltViewModel(),
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
    val pillsDescriptions by viewModel.pillsDescriptions.collectAsStateWithLifecycle()
    val isLoadingInProgress by viewModel.isLoadingInProgress.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is PillsEvents.NavigateToAddPill -> onNavigateToAddPill()
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.fetchPillsDescriptions() }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = WindowInsets.statusBars.add(
                WindowInsets(
                    left = 16.dp,
                    right = 16.dp,
                    top = 8.dp,
                    bottom = bottomContentPadding.dp
                )
            ).asPaddingValues(),
        ) {
            items(pillsDescriptions) { pillDescription ->
                PillCard(
                    modifier = Modifier.padding(bottom = 4.dp),
                    pillDescriptionUI = pillDescription,
                    onClick = { viewModel.onPillCardClick(pillDescription) },
                    onRemoveClick = { viewModel.onPillCardRemoveClick(pillDescription) }
                )
            }
        }
        AnimatedVisibility(visible = isLoadingInProgress) {
            InfiniteProgressIndicator(
                modifier = Modifier.padding(bottom = bottomContentPadding.dp)
            )
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.Center),
            visible = pillsDescriptions.isEmpty() && !isLoadingInProgress
        ) {
            Text(
                text = stringResource(R.string.there_are_no_medicines),
                style = MaterialTheme.typography.titleMedium,
                color = LightRedColor
            )
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

// TODO fix this to correctly work with Hilt
//@Preview
//@Composable
//private fun PillsScreenPreview() {
//    PillsScreen(viewModel = PillsViewModelMockImpl(), onNavigateToAddPill = {})
//}