package com.rey.dumbdumb.domain.usecase.repository

import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher
import javax.crypto.SecretKey

internal interface ISecureRepository {
    suspend fun getSecretKey(alias: String): Result<SecretKey>
    suspend fun getEncryptionCipher(secretKey: SecretKey): Result<Cipher>
    suspend fun getDecryptionCipher(
        secretKey: SecretKey,
        initializationVector: ByteArray
    ): Result<Cipher>
}