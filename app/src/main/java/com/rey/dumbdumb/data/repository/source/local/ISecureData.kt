package com.rey.dumbdumb.data.repository.source.local

import javax.crypto.Cipher
import javax.crypto.SecretKey

internal interface ISecureData {
    suspend fun generateSecretKey()
    fun getCipher(): Cipher
    fun getSecretKey(): SecretKey
}