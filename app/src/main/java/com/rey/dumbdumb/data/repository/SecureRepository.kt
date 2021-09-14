package com.rey.dumbdumb.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.rey.dumbdumb.data.repository.source.local.ISecureData
import com.rey.dumbdumb.data.repository.source.local.ISecureLocalData
import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.data
import com.rey.lib.cleanarch.domain.dto.next
import com.rey.lib.cleanarch.domain.dto.nextOnError
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject

internal class SecureRepository @Inject constructor(
    private val secureData: ISecureData,
    private val secureLocalData: ISecureLocalData
) : ISecureRepository {
    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getSecretKey(alias: String): Result<SecretKey> =
        secureData.getSecretKey(KEY_STORE, alias).nextOnError {
            secureData.generateSecretKey(KEY_STORE, alias)
                .next { secureData.getSecretKey(KEY_STORE, alias) }
        }

    @RequiresApi(Build.VERSION_CODES.M)
    override suspend fun getEncryptionCipher(secretKey: SecretKey): Result<Cipher> =
        secureData.getCipher().next { cipherResult ->
            cipherResult.data.init(Cipher.ENCRYPT_MODE, secretKey)
            cipherResult
        }

    override suspend fun getDecryptionCipher(
        secretKey: SecretKey,
        initializationVector: ByteArray
    ): Result<Cipher> = secureData.getCipher().next { cipherResult ->
        cipherResult.data.init(
            Cipher.DECRYPT_MODE,
            secretKey,
            GCMParameterSpec(128, initializationVector)
        )
        cipherResult
    }

    override suspend fun encrypt(plaintext: String, cipher: Cipher): Result<EncryptRes> =
        secureData.encrypt(plaintext, cipher)

    override suspend fun decrypt(ciphertext: ByteArray, cipher: Cipher): Result<String> =
        secureData.decrypt(ciphertext, cipher)

    override suspend fun storeCredential(data: EncryptReq): Result<Unit> =
        secureLocalData.storeCredential(data)

    override suspend fun getCredential(): Result<EncryptRes> = secureLocalData.getCredential()

    override suspend fun decode(token: String): Result<String> = secureData.decode(token)

    companion object {
        private const val KEY_STORE = "AndroidKeyStore"
    }
}