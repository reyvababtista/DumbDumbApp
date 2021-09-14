package com.rey.dumbdumb.domain.usecase.repository

import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
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

    suspend fun encrypt(plaintext: String, cipher: Cipher): Result<EncryptRes>
    suspend fun decrypt(ciphertext: ByteArray, cipher: Cipher): Result<String>
    suspend fun storeCredential(data: EncryptReq): Result<Unit>
    suspend fun getCredential(): Result<EncryptRes>
    suspend fun decode(token: String): Result<String>
}