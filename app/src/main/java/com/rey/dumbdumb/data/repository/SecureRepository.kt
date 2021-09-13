package com.rey.dumbdumb.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.rey.dumbdumb.data.source.local.SecureData
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.onError
import com.rey.lib.cleanarch.domain.dto.onSuccess
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.inject.Inject

internal class SecureRepository @Inject constructor(private val data: SecureData) :
    ISecureRepository {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getSecretKey(keyProvider: String, alias: String): Result<SecretKey> =
        data.getSecretKey(keyProvider, alias).onError {
            data.generateSecretKey(keyProvider, alias)
                .onSuccess { data.getSecretKey(keyProvider, alias) }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun getCipher(): Result<Cipher> = data.getCipher()
}