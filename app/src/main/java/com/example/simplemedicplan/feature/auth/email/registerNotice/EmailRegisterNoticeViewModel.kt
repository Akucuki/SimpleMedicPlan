package com.example.simplemedicplan.feature.auth.email.registerNotice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplemedicplan.persistance.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailRegisterNoticeViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    val events = Channel<EmailRegisterNoticeEvents>(Channel.UNLIMITED)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val isEmailVerificationSent = dataStoreManager.getIsEmailVerificationSent() ?: false
            if (!isEmailVerificationSent) {
                events.trySend(EmailRegisterNoticeEvents.SendEmailConfirmation)
                dataStoreManager.setIsEmailVerificationSent(true)
            }
            events.trySend(EmailRegisterNoticeEvents.FetchIsCurrentUserEmailVerified)
        }
    }

    fun onEmailVerificationFailed() {
        events.trySend(EmailRegisterNoticeEvents.ShowErrorToast)
    }

    fun onFetchIsCurrentUserEmailVerifiedFinished(isEmailVerified: Boolean) {
        if (isEmailVerified) {
            events.trySend(EmailRegisterNoticeEvents.NavigateToHome)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                delay(1000)
                events.trySend(EmailRegisterNoticeEvents.FetchIsCurrentUserEmailVerified)
            }
        }
    }
}