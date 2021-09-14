package com.rey.dumbdumb.data.source.local

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.auth0.jwt.JWT
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
internal class SecureData @Inject constructor(private val jwt: JWT) : ISecureData {

    override fun getSecretKey(keyProvider: String, alias: String): Result<SecretKey> = tryCatch {
        val keyStore = KeyStore.getInstance(keyProvider)
        keyStore.load(null)
        val secretKey = keyStore.getKey(alias, null)?.let { it as SecretKey }
            ?: throw NullPointerException("key not found")
        Result.Success(secretKey)
    }

    override suspend fun getCipher(): Result<Cipher> = suspendTryCatch {
        val cipher = Cipher.getInstance(
            "$ENCRYPTION_ALGORITHM/$ENCRYPTION_BLOCK_MODE/$ENCRYPTION_PADDING"
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
                .setBlockModes(ENCRYPTION_BLOCK_MODE)
                .setEncryptionPaddings(ENCRYPTION_PADDING)
                .setKeySize(KEY_SIZE)
                .setUserAuthenticationRequired(true)
                .setInvalidatedByBiometricEnrollment(true)
                .build()

            val keyGenerator = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM, keyProvider)
            keyGenerator.init(keyGenParamSpec)
            keyGenerator.generateKey()

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

    override suspend fun decode(token: String): Result<String> = suspendTryCatch {
        val data: String = jwt.decodeJwt(token).claims["name"].toString()
        Result.Success(data)
    }

    companion object {
        private const val UTF_8 = "UTF-8"
        private const val KEY_SIZE = 256
        private const val ENCRYPTION_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val ENCRYPTION_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val ENCRYPTION_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
    }
}