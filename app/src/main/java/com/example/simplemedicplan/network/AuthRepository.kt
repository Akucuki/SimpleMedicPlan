package com.example.simplemedicplan.network

import com.google.android.gms.auth.api.identity.SignInClient
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private var oneTapClient: SignInClient
) {
}