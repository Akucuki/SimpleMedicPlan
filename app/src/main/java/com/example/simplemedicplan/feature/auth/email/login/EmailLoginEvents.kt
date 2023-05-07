package com.example.simplemedicplan.feature.auth.email.login

sealed class EmailLoginEvents {
    object NavigateBack : EmailLoginEvents()
    object NavigateToHome : EmailLoginEvents()
    data class Login(val email: String, val password: String) : EmailLoginEvents()
    object ShowErrorToast : EmailLoginEvents()
}
