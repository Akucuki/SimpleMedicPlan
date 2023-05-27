package com.example.simplemedicplan.feature.home.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.YellowColor
import com.example.simplemedicplan.feature.common.PrimaryButton
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onNavigateToAuth: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val userDisplayName = viewModel.userDisplayName ?: stringResource(R.string.uknown_name)

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is ProfileEvents.NavigateToAuth -> onNavigateToAuth()
            }
        }
    }

    Column(
        modifier = Modifier
            .statusBarsPadding()
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = userDisplayName,
            style = MaterialTheme.typography.titleLarge,
            color = YellowColor
        )
        PrimaryButton(
            text = stringResource(R.string.sign_out),
            onClick = viewModel::onSignOutClick
        )
    }
}