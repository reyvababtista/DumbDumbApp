package com.rey.dumbdumb.data.source.local

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import com.rey.dumbdumb.data.repository.source.local.ISecureData
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.suspendTryCatch
import com.rey.lib.cleanarch.domain.dto.tryCatch
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

@RequiresApi(Build.VERSION_CODES.M)
internal class SecureData : ISecureData {
    private fun generateSecretKey(keyGenParameterSpec: KeyGenParameterSpec) {
        val keyGenerator = KeyGenerator.getInstance(KEY_PROVIDER)
        keyGenerator.init(keyGenParameterSpec)
        keyGenerator.generateKey()
    }

    override fun getSecretKey(): Result<SecretKey> = tryCatch {
        val keyStore = KeyStore.getInstance(KEY_PROVIDER)

        // Before the keystore can be accessed, it must be loaded.
        keyStore.load(null)
        val secretKey = keyStore.getKey(KEY_ALIAS, null) as SecretKey
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
    override suspend fun generateSecretKey() {
        val keyGenParamSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
            .setUserAuthenticationRequired(true)
            .setInvalidatedByBiometricEnrollment(true)
            .build()

        generateSecretKey(keyGenParamSpec)
    }

    companion object {
        private const val KEY_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "Foo"
    }
}