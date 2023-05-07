package com.example.simplemedicplan.feature.auth.email.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.simplemedicplan.feature.auth.email.register.EMAIL
import com.example.simplemedicplan.feature.auth.email.register.PASSWORD
import com.example.simplemedicplan.model.TextFieldValueWrapper
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class EmailLoginViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val events = Channel<EmailLoginEvents>(Channel.UNLIMITED)
    val email = handle.getStateFlow(EMAIL, TextFieldValueWrapper())
    val password = handle.getStateFlow(PASSWORD, TextFieldValueWrapper())

    fun onEmailValueChange(value: String) {
        handle[EMAIL] = email.value.copy(value = value)
    }

    fun onPasswordValueChange(value: String) {
        handle[PASSWORD] = password.value.copy(value = value)
    }

    fun onBackArrowClick() {
        events.trySend(EmailLoginEvents.NavigateBack)
    }

    fun onLoginClick() {
        events.trySend(EmailLoginEvents.Login(email.value.value, password.value.value))
    }

    fun onLoginSuccess() {
        events.trySend(EmailLoginEvents.NavigateToHome)
    }

    fun onLoginFailure() {
        events.trySend(EmailLoginEvents.ShowErrorToast)
    }
}