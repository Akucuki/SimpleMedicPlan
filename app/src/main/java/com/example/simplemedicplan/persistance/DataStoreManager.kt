package com.example.simplemedicplan.persistance

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

val Context.getDataStore: DataStore<Preferences> by preferencesDataStore(name = "simpleMedicPlanDataStore")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.getDataStore

    suspend fun setIsEmailVerificationSent(isEmailVerificationSent: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.isEmailVerificationSent] = isEmailVerificationSent
        }
    }

    suspend fun getIsEmailVerificationSent(): Boolean? =
        dataStore.data.first()[PreferencesKeys.isEmailVerificationSent]

    private object PreferencesKeys {
        val isEmailVerificationSent = booleanPreferencesKey("isEmailVerificationSent")
    }
}