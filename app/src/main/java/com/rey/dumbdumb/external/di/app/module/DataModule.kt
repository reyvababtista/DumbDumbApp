package com.rey.dumbdumb.external.di.app.module

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.auth0.jwt.JWT
import com.google.gson.Gson
import com.rey.dumbdumb.external.di.app.annotation.ApplicationContext
import com.rey.lib.cleanarch.data.repository.source.local.ISharedPrefs
import com.rey.lib.cleanarch.data.source.local.SharedPrefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class DataModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideSecureSharedPref(@ApplicationContext context: Context): ISharedPrefs {
        val encryptedSharedPreferences = EncryptedSharedPreferences.create(
            "default",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        return SharedPrefs(encryptedSharedPreferences)
    }

    @Provides
    @Singleton
    fun provideJwt(): JWT = JWT()
}