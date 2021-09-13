package com.rey.dumbdumb.data.source.local

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.rey.dumbdumb.data.repository.source.local.ISecureData
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.suspendTryCatch
import com.rey.lib.cleanarch.domain.dto.tryCatch
import java.nio.charset.Charset
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.M)
internal class SecureData @Inject constructor() : ISecureData {

    override fun getSecretKey(keyProvider: String, alias: String): Result<SecretKey> = tryCatch {
        val keyStore = KeyStore.getInstance(keyProvider)
        keyStore.load(null)
        val secretKey = keyStore.getKey(alias, null) as SecretKey
        Result.Success(secretKey)
    }

    override suspend fun getCipher(): Result<Cipher> = suspendTryCatch {
        val cipher = Cipher.getInstance(
            KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7
        )
        Result.Success(cipher)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun generateSecretKey(keyProvider: String, alias: String): Result<Unit> =
        suspendTryCatch {
            val keyGenParamSpec = KeyGenParameterSpec.Builder(
                alias,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setUserAuthenticationRequired(true)
                .setInvalidatedByBiometricEnrollment(true)
                .build()

            KeyGenerator.getInstance(keyProvider).run {
                init(keyGenParamSpec)
                generateKey()
            }

            Result.Success(Unit)
        }

    override suspend fun encrypt(plaintext: String, cipher: Cipher): Result<EncryptRes> =
        suspendTryCatch {
            val ciphertext = cipher.doFinal(plaintext.toByteArray(Charset.forName(UTF_8)))
            Result.Success(EncryptRes(ciphertext, cipher.iv))
        }

    override suspend fun decrypt(ciphertext: ByteArray, cipher: Cipher): Result<String> =
        suspendTryCatch {
            val plaintext = cipher.doFinal(ciphertext)
            Result.Success(String(plaintext, Charset.forName(UTF_8)))
        }

    companion object {
        private const val UTF_8 = "UTF-8"
    }
}