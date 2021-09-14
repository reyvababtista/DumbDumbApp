package com.rey.dumbdumb.domain.entity.usecase

import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher

interface ISecureUseCase {
    suspend fun getEncryptionCipher(keyAlias: String): Result<Cipher>
    suspend fun getDecryptionCipher(
        keyAlias: String,
        initializationVector: ByteArray
    ): Result<Cipher>

    suspend fun storeEncryptedCredential(data: EncryptReq): Result<Unit>
    suspend fun getEncryptedCredential(): Result<EncryptRes>
    suspend fun encrypt(plaintext: String, cipher: Cipher): Result<EncryptRes>
    suspend fun decrypt(ciphertext: ByteArray, cipher: Cipher): Result<String>
    suspend fun decode(token: String): Result<String>
}