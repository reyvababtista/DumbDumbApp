package com.rey.dumbdumb.domain.usecase

import com.rey.dumbdumb.domain.entity.usecase.ISecureUseCase
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.onSuccess
import javax.crypto.Cipher
import javax.inject.Inject

internal class SecureUseCase @Inject constructor(private val repository: ISecureRepository) :
    ISecureUseCase {
    override suspend fun generateCipher(): Result<Cipher> =
        repository.getCipher().onSuccess { cipher ->
            repository.getSecretKey(KEY_PROVIDER, KEY_ALIAS).onSuccess { secretKey ->
                cipher.init(Cipher.ENCRYPT_MODE, secretKey)
                Result.Success(cipher)
            }
        }

    companion object {
        private const val KEY_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "Biometric"
    }
}