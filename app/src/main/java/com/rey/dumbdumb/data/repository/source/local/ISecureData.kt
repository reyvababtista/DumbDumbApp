package com.rey.dumbdumb.data.repository.source.local

import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher
import javax.crypto.SecretKey

internal interface ISecureData {
    suspend fun generateSecretKey()
    fun getSecretKey(): Result<SecretKey>
    suspend fun getCipher(): Result<Cipher>
}