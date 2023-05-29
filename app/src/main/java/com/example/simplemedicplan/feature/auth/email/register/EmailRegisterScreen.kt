package com.example.simplemedicplan.feature.auth.email.register

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
fun EmailRegisterScreen(
    viewModel: EmailRegisterViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToRegistrationNotice: () -> Unit
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
    val passwordConfirm by viewModel.passwordConfirm.collectAsStateWithLifecycle()
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
    var isRepeatedPasswordVisible by remember { mutableStateOf(false) }
    val repeatedPasswordVisualTransformation = remember (isRepeatedPasswordVisible) {
        if (isRepeatedPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    }
    val repeatedPasswordVisibilityIconId = remember (isRepeatedPasswordVisible) {
        if (isRepeatedPasswordVisible) {
            R.drawable.ic_visibility_off
        } else {
            R.drawable.ic_visibility
        }
    }

    suspend fun register(email: String, password: String) {
        // TODO handle collision
        withContext(Dispatchers.IO) {
            try {
                val registerTask = Firebase.auth.createUserWithEmailAndPassword(email, password)
                Tasks.await(registerTask)
                when {
                    registerTask.isSuccessful -> viewModel.onRegisterSuccess()
                    else -> viewModel.onRegisterFailure()
                }
            } catch (e: Exception) {
                viewModel.onRegisterFailure()
            }
        }
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                is EmailRegisterEvents.NavigateBack -> onNavigateBack()
                is EmailRegisterEvents.NavigateToRegistrationNotice -> {
                    onNavigateToRegistrationNotice()
                }
                is EmailRegisterEvents.Register -> {
                    val (lastEmail, lastPassword) = event
                    register(lastEmail, lastPassword)
                }
                is EmailRegisterEvents.ShowErrorToast -> {
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
            title = stringResource(R.string.register),
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
            PrimaryTextField(
                modifier = Modifier.fillMaxWidth(),
                value = passwordConfirm,
                onValueChange = viewModel::onPasswordConfirmValueChange,
                labelText = stringResource(R.string.re_enter_password),
                visualTransformation = repeatedPasswordVisualTransformation,
                trailingIcon = {
                    IconButton(onClick = { isRepeatedPasswordVisible = !isRepeatedPasswordVisible }) {
                        Icon(
                            painter = painterResource(repeatedPasswordVisibilityIconId),
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
                text = stringResource(R.string.register),
                onClick = viewModel::onRegisterClick
            )
        }
    }
}