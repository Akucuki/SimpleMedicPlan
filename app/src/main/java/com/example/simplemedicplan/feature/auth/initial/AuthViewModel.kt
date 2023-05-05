package com.example.simplemedicplan.feature.auth.initial

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val events = Channel<AuthEvents>(Channel.UNLIMITED)

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