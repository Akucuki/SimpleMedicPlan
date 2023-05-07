package com.example.simplemedicplan.feature.auth.email.registerNotice

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import com.example.simplemedicplan.R
import com.example.simplemedicplan.application.theme.YellowColor
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Composable
fun EmailRegistrationNoticeScreen(
    viewModel: EmailRegisterNoticeViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val events = remember(viewModel.events, lifecycleOwner) {
        viewModel.events.receiveAsFlow().flowWithLifecycle(
            lifecycleOwner.lifecycle,
            Lifecycle.State.STARTED
        )
    }

    suspend fun sendEmailConfirmation() {
        withContext(Dispatchers.IO) {
            try {
                val verificationTask = Firebase.auth.currentUser!!.sendEmailVerification()
                Tasks.await(verificationTask)
            } catch (e: Exception) {
                viewModel.onEmailVerificationFailed()
            }
        }
    }

    suspend fun fetchIsCurrentUserEmailVerified() {
        FirebaseAuth.getInstance().currentUser?.reload()?.await()
        val isEmailVerified = FirebaseAuth.getInstance().currentUser?.isEmailVerified ?: false
        viewModel.onFetchIsCurrentUserEmailVerifiedFinished(isEmailVerified)
    }

    LaunchedEffect(Unit) {
        events.collect { event ->
            when (event) {
                EmailRegisterNoticeEvents.SendEmailConfirmation -> sendEmailConfirmation()
                EmailRegisterNoticeEvents.NavigateToHome -> onNavigateToHome()
                EmailRegisterNoticeEvents.ShowErrorToast -> {
                    Toast.makeText(
                        context,
                        R.string.error_too_many_requests,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                EmailRegisterNoticeEvents.FetchIsCurrentUserEmailVerified -> {
                    fetchIsCurrentUserEmailVerified()
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.please_check_email_instructions),
            style = MaterialTheme.typography.bodyLarge,
            color = YellowColor,
            textAlign = TextAlign.Center
        )
    }
}