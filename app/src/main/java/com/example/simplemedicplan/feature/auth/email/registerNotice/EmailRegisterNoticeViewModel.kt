package com.example.simplemedicplan.feature.auth.email.registerNotice

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

class EmailRegisterNoticeViewModel @Inject constructor() : ViewModel() {

    val events = Channel<EmailRegisterNoticeEvents>(Channel.UNLIMITED)

    init { events.trySend(EmailRegisterNoticeEvents.SendEmailConfirmation) }
}