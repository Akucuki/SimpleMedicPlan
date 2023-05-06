package com.example.simplemedicplan.feature.auth.email.register

sealed class EmailRegisterEvents {
    object NavigateBack : EmailRegisterEvents()
    object NavigateToRegistrationNotice : EmailRegisterEvents()
}
