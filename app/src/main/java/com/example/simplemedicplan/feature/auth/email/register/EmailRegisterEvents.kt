package com.example.simplemedicplan.feature.auth.email.register

sealed class EmailRegisterEvents {
    object NavigateBack : EmailRegisterEvents()
    object NavigateToRegistrationNotice : EmailRegisterEvents()
    data class Register(val email: String, val password: String) : EmailRegisterEvents()
    object ShowErrorToast : EmailRegisterEvents()
}
