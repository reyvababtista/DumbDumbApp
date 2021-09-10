package com.rey.dumbdumb.domain.usecase.repository

import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher
import javax.crypto.SecretKey

internal interface ISecureRepository {
    suspend fun generateSecretKey(keyProvider: String, alias: String): Result<Unit>
    fun getSecretKey(keyProvider: String, alias: String): Result<SecretKey>
    suspend fun getCipher(): Result<Cipher>
}