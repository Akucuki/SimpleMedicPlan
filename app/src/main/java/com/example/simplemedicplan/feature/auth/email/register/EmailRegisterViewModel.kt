package com.example.simplemedicplan.feature.auth.email.register

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.simplemedicplan.model.TextFieldValueWrapper
import com.example.simplemedicplan.utils.InputValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

const val EMAIL = "email"
const val PASSWORD = "password"
private const val PASSWORD_CONFIRM = "password_confirm"

@HiltViewModel
class EmailRegisterViewModel @Inject constructor(
    private val handle: SavedStateHandle
) : ViewModel() {

    val events = Channel<EmailRegisterEvents>(Channel.UNLIMITED)
    val email = handle.getStateFlow(EMAIL, TextFieldValueWrapper())
    val password = handle.getStateFlow(PASSWORD, TextFieldValueWrapper())
    val passwordConfirm = handle.getStateFlow(PASSWORD_CONFIRM, TextFieldValueWrapper())

    fun onEmailValueChange(value: String) {
        val errorId = InputValidator.getEmailErrorIdOrNull(value)
        handle[EMAIL] = email.value.copy(value = value, errorId = errorId)
    }

    fun onPasswordValueChange(value: String) {
        val errorId = InputValidator.getPasswordErrorIdOrNull(value)
        handle[PASSWORD] = password.value.copy(value = value, errorId = errorId)
    }

    fun onPasswordConfirmValueChange(value: String) {
        val errorId = InputValidator.getPasswordMismatchErrorIdOrNull(password.value.value, value)
        handle[PASSWORD_CONFIRM] = passwordConfirm.value.copy(value = value, errorId = errorId)
    }

    fun onBackArrowClick() {
        events.trySend(EmailRegisterEvents.NavigateBack)
    }

    fun onRegisterClick() {
        events.trySend(EmailRegisterEvents.Register(email.value.value, password.value.value))
    }

    fun onRegisterSuccess() {
        events.trySend(EmailRegisterEvents.NavigateToRegistrationNotice)
    }

    fun onRegisterFailure() {
        events.trySend(EmailRegisterEvents.ShowErrorToast)
    }
}