package com.example.simplemedicplan.feature.auth.email.registerNotice

sealed class EmailRegisterNoticeEvents {
    object SendEmailConfirmation : EmailRegisterNoticeEvents()
    object NavigateToHome : EmailRegisterNoticeEvents()
    object ShowErrorToast : EmailRegisterNoticeEvents()
    object FetchIsCurrentUserEmailVerified : EmailRegisterNoticeEvents()
}
