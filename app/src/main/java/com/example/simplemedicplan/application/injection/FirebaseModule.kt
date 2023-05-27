package com.example.simplemedicplan.application.injection

import com.example.simplemedicplan.utils.FIREBASE_DATABASE_URL
import com.example.simplemedicplan.utils.FIREBASE_DATABASE_USERS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    fun provideUserTiedDatabaseReference(): DatabaseReference {
        val databaseReference =
            Firebase.database(FIREBASE_DATABASE_URL).getReference(FIREBASE_DATABASE_USERS)
        val userId = Firebase.auth.currentUser?.uid!!
        return databaseReference.child(userId)
    }
}