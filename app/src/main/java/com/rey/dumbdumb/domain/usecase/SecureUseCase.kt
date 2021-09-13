package com.rey.dumbdumb.domain.usecase

import com.rey.dumbdumb.domain.entity.usecase.ISecureUseCase
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.data
import com.rey.lib.cleanarch.domain.dto.next
import javax.crypto.Cipher
import javax.inject.Inject

internal class SecureUseCase @Inject constructor(private val repository: ISecureRepository) :
    ISecureUseCase {
    override suspend fun generateCipher(): Result<Cipher> =
        repository.generateSecretKey(KEY_PROVIDER, KEY_ALIAS).next {
            repository.getCipher().next { cipherResult ->
                repository.getSecretKey(KEY_PROVIDER, KEY_ALIAS).next { secretKeyResult ->
                    val cipher = cipherResult.data
                    cipher.init(Cipher.ENCRYPT_MODE, secretKeyResult.data)
                    Result.Success(cipher)
                }
            }
        }

    companion object {
        private const val KEY_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "Biometric"
    }
}