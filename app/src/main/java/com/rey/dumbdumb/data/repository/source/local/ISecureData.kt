package com.rey.dumbdumb.data.repository.source.local

import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher
import javax.crypto.SecretKey

internal interface ISecureData {
    suspend fun generateSecretKey(keyProvider: String, alias: String)
    fun getSecretKey(keyProvider: String, alias: String): Result<SecretKey>
    suspend fun getCipher(): Result<Cipher>
}