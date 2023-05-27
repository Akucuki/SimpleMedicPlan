package com.example.simplemedicplan.feature.home.profile

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(

) : ViewModel() {

    val events = Channel<ProfileEvents>(Channel.UNLIMITED)
    val userDisplayName = Firebase.auth.currentUser?.displayName

    fun onSignOutClick() {
        Firebase.auth.signOut()
        events.trySend(ProfileEvents.NavigateToAuth)
    }
}