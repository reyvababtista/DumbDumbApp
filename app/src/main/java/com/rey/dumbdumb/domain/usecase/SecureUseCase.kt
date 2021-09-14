package com.rey.dumbdumb.domain.usecase

import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.dumbdumb.domain.entity.usecase.ISecureUseCase
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.data
import com.rey.lib.cleanarch.domain.dto.next
import javax.crypto.Cipher
import javax.inject.Inject

internal class SecureUseCase @Inject constructor(private val repository: ISecureRepository) :
    ISecureUseCase {
    override suspend fun getEncryptionCipher(keyAlias: String): Result<Cipher> =
        repository.getSecretKey(keyAlias).next {
            repository.getEncryptionCipher(it.data)
        }

    override suspend fun getDecryptionCipher(
        keyAlias: String,
        initializationVector: ByteArray
    ): Result<Cipher> = repository.getSecretKey(keyAlias).next {
        repository.getDecryptionCipher(it.data, initializationVector)
    }

    override suspend fun storeEncryptedCredential(data: EncryptReq): Result<Unit> =
        repository.storeCredential(data)

    override suspend fun getEncryptedCredential(): Result<EncryptRes> = repository.getCredential()

    override suspend fun encrypt(plaintext: String, cipher: Cipher): Result<EncryptRes> =
        repository.encrypt(plaintext, cipher)

    override suspend fun decrypt(ciphertext: ByteArray, cipher: Cipher): Result<String> =
        repository.decrypt(ciphertext, cipher)

    override suspend fun decode(token: String): Result<String> = repository.decode(token)
}