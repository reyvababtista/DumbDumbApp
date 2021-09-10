package com.rey.dumbdumb.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.rey.dumbdumb.data.source.local.SecureData
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
internal class SecureRepository @Inject constructor(private val secureData: SecureData) :
    ISecureRepository {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun generateSecretKey() = secureData.generateSecretKey()

    override fun getSecretKey(): Result<SecretKey> = secureData.getSecretKey()

    override suspend fun getCipher(): Result<Cipher> = secureData.getCipher()
}