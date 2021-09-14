package com.rey.dumbdumb.domain.entity.usecase

import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher

interface IAuthUseCase {
    suspend fun authenticate(username: String, password: String): Result<String>
    suspend fun authenticate(
        encryptionCipher: Cipher?,
        decryptionCipher: Cipher?,
        token: String?,
        encryptedCredential: EncryptReq?
    ): Result<String>

    suspend fun getEncryptedCredential(): Result<EncryptRes>
    suspend fun getEncryptionCipher(): Result<Cipher>
    suspend fun getDecryptionCipher(initializationVector: ByteArray): Result<Cipher>
}