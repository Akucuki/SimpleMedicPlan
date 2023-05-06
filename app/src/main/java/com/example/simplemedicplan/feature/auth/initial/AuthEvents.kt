package com.example.simplemedicplan.feature.auth.initial

sealed class AuthEvents {
    object AuthWithGoogle : AuthEvents()
    object AuthWithFacebook : AuthEvents()
    object NavigateHome : AuthEvents()
    object NavigateEmailLogin : AuthEvents()
    object NavigateEmailRegister : AuthEvents()
    object ShowErrorToast : AuthEvents()
}