package com.rey.dumbdumb.domain.usecase

import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.dumbdumb.domain.entity.usecase.IAuthUseCase
import com.rey.dumbdumb.domain.entity.usecase.ISecureUseCase
import com.rey.dumbdumb.domain.usecase.repository.IAuthRepository
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.data
import com.rey.lib.cleanarch.domain.dto.next
import com.rey.lib.cleanarch.domain.dto.suspendTryCatch
import javax.crypto.Cipher
import javax.inject.Inject

internal class AuthUseCase @Inject constructor(
    private val repository: IAuthRepository,
    private val secureUseCase: ISecureUseCase
) : IAuthUseCase {

    override suspend fun authenticate(username: String, password: String): Result<String> =
        repository.authenticate(username, password)

    override suspend fun authenticate(
        encryptionCipher: Cipher?,
        decryptionCipher: Cipher?,
        token: String?,
        encryptedCredential: EncryptReq?
    ): Result<String> = suspendTryCatch {
        // user already has credential stored securely
        if (encryptedCredential != null && decryptionCipher != null && token == null) {
            secureUseCase.decrypt(encryptedCredential.ciphertext, decryptionCipher)
                .next { secureUseCase.decode(it.data) }
        } else if (token != null && encryptionCipher != null && encryptedCredential == null) {
            secureUseCase.encrypt(token, encryptionCipher)
                .next {
                    val encryptRes = it.data
                    secureUseCase.storeEncryptedCredential(
                        EncryptReq(
                            encryptRes.ciphertext,
                            encryptRes.initializationVector
                        )
                    )
                }
                .next { secureUseCase.decode(token) }
        } else {
            throw IllegalStateException("authentication failec")
        }
    }

    override suspend fun getEncryptedCredential(): Result<EncryptRes> =
        secureUseCase.getEncryptedCredential()

    override suspend fun getEncryptionCipher() = secureUseCase.getEncryptionCipher(SECRET_KEY_ALIAS)

    override suspend fun getDecryptionCipher(initializationVector: ByteArray) =
        secureUseCase.getDecryptionCipher(SECRET_KEY_ALIAS, initializationVector)

    companion object {
        private const val SECRET_KEY_ALIAS = "Biometric"
    }
}