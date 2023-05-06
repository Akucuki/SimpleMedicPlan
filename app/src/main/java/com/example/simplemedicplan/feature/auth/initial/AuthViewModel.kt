package com.example.simplemedicplan.feature.auth.initial

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor() : ViewModel() {

    val events = Channel<AuthEvents>(Channel.UNLIMITED)

    fun onEmailLoginClick() {
        events.trySend(AuthEvents.NavigateEmailLogin)
    }

    fun onEmailRegisterClick() {
        events.trySend(AuthEvents.NavigateEmailRegister)
    }

    fun onAuthWithGoogleClick() {
        events.trySend(AuthEvents.AuthWithGoogle)
    }

    fun onAuthWithFacebookClick() {
        events.trySend(AuthEvents.AuthWithFacebook)
    }

    fun onAuthSuccess() {
        events.trySend(AuthEvents.NavigateHome)
    }

    fun onAuthFailure() {
        events.trySend(AuthEvents.ShowErrorToast)
    }
}