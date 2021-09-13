package com.rey.dumbdumb.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.rey.dumbdumb.data.repository.source.local.ISecureData
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.data
import com.rey.lib.cleanarch.domain.dto.next
import com.rey.lib.cleanarch.domain.dto.nextOnError
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

internal class SecureRepository @Inject constructor(private val data: ISecureData) :
    ISecureRepository {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getSecretKey(alias: String): Result<SecretKey> =
        data.getSecretKey(KEY_STORE, alias).nextOnError {
            data.generateSecretKey(KEY_STORE, alias)
                .next { data.getSecretKey(KEY_STORE, alias) }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun getEncryptionCipher(secretKey: SecretKey): Result<Cipher> =
        data.getCipher().next { cipherResult ->
            cipherResult.data.init(Cipher.ENCRYPT_MODE, secretKey)
            cipherResult
        }

    override suspend fun getDecryptionCipher(
        secretKey: SecretKey,
        initializationVector: ByteArray
    ): Result<Cipher> = data.getCipher().next { cipherResult ->
        cipherResult.data.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(128, initializationVector)
        )
        cipherResult
    }

    companion object {
        private const val KEY_STORE = "AndroidKeyStore"
    }
}