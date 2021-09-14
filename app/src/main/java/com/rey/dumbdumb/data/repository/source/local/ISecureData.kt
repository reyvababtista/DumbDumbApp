package com.rey.dumbdumb.data.repository.source.local

import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher
import javax.crypto.SecretKey

internal interface ISecureData {
    suspend fun generateSecretKey(keyProvider: String, alias: String): Result<Unit>
    fun getSecretKey(keyProvider: String, alias: String): Result<SecretKey>
    suspend fun getCipher(): Result<Cipher>
    suspend fun encrypt(plaintext: String, cipher: Cipher): Result<EncryptRes>
    suspend fun decrypt(ciphertext: ByteArray, cipher: Cipher): Result<String>
    suspend fun decode(token: String): Result<String>
}