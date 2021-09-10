package com.rey.dumbdumb.domain.entity.usecase

import com.rey.lib.cleanarch.domain.dto.Result
import javax.crypto.Cipher

interface ISecureUseCase {
    suspend fun generateCipher(): Result<Cipher>
}