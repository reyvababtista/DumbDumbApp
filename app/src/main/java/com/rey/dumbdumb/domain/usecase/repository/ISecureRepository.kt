package com.rey.dumbdumb.domain.usecase.repository

import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher
import javax.crypto.SecretKey

internal interface ISecureRepository {
    suspend fun generateSecretKey()
    fun getSecretKey(): Result<SecretKey>
    suspend fun getCipher(): Result<Cipher>
}