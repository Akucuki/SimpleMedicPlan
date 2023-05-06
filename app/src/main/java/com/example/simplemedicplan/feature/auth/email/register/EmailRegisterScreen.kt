package com.example.simplemedicplan.feature.auth.email.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.R
import com.example.simplemedicplan.feature.common.PrimaryButton
import com.example.simplemedicplan.feature.common.PrimaryTextField
import com.example.simplemedicplan.feature.common.SMpAppBar
import com.example.simplemedicplan.utils.APP_BAR_HEIGHT
import kotlinx.coroutines.flow.receiveAsFlow

@Composable
fun EmailAuthScreen(
    viewModel: EmailRegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToRegistrationNotice: () -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val passwordConfirm by viewModel.passwordConfirm.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is EmailRegisterEvents.NavigateBack -> onNavigateBack()
                is EmailRegisterEvents.NavigateToRegistrationNotice -> {
                    onNavigateToRegistrationNotice()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SMpAppBar(
            title = "Login",
            leadingIcon = Icons.Filled.ArrowBack,
            onLeadingIconClick = viewModel::onBackArrowClick
        )
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = APP_BAR_HEIGHT.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = email,
                onValueChange = viewModel::onEmailValueChange,
                labelText = stringResource(R.string.email)
            )
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = viewModel::onPasswordValueChange,
                labelText = stringResource(R.string.password)
            )
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordConfirm,
                onValueChange = viewModel::onPasswordConfirmValueChange,
                labelText = stringResource(R.string.re_enter_password)
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier.navigationBarsPadding(),
                text = stringResource(R.string.register),
                onClick = viewModel::onRegisterClick
            )
        }
    }
}