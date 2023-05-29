package com.example.simplemedicplan.feature.auth.email.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext

@Composable
fun EmailLoginScreen(
    viewModel: EmailLoginViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordVisualTransformation = remember (isPasswordVisible) {
        if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    }
    val passwordVisibilityIconId = remember (isPasswordVisible) {
        if (isPasswordVisible) {
            R.drawable.ic_visibility_off
        } else {
            R.drawable.ic_visibility
        }
    }

    suspend fun login(email: String, password: String) {
        // TODO handle collision
        withContext(Dispatchers.IO) {
            try {
                val loginTask = Firebase.auth.signInWithEmailAndPassword(email, password)
                Tasks.await(loginTask)
                when {
                    loginTask.isSuccessful -> viewModel.onLoginSuccess()
                    else -> viewModel.onLoginFailure()
                }
            } catch (e: Exception) {
                viewModel.onLoginFailure()
            }
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is EmailLoginEvents.NavigateBack -> onNavigateBack()
                is EmailLoginEvents.NavigateToHome -> {
                    onNavigateToHome()
                }
                is EmailLoginEvents.Login -> {
                    val (lastEmail, lastPassword) = event
                    login(lastEmail, lastPassword)
                }
                is EmailLoginEvents.ShowErrorToast -> {
                    Toast.makeText(
                        context,
                        R.string.error_failed_to_authorize,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        SMpAppBar(
            title = stringResource(R.string.login),
            leadingIcon = Icons.Filled.ArrowBack,
            onLeadingIconClick = viewModel::onBackArrowClick
        )
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = APP_BAR_HEIGHT.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            PrimaryTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                value = email,
                onValueChange = viewModel::onEmailValueChange,
                labelText = stringResource(R.string.email)
            )
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = password,
                onValueChange = viewModel::onPasswordValueChange,
                labelText = stringResource(R.string.password),
                visualTransformation = passwordVisualTransformation,
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            painter = painterResource(passwordVisibilityIconId),
                            null
                        )
                    }
                },
            )
            Spacer(modifier = Modifier.weight(1f))
            PrimaryButton(
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(bottom = 16.dp),
                text = stringResource(R.string.login),
                onClick = viewModel::onLoginClick
            )
        }
    }
}