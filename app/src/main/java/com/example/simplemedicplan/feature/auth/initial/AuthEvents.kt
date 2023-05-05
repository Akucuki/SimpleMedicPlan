package com.example.simplemedicplan.feature.auth.initial

sealed class AuthEvents {
    object AuthWithGoogle : AuthEvents()
    object AuthWithFacebook : AuthEvents()
    object NavigateHome : AuthEvents()
    object ShowErrorToast : AuthEvents()
}