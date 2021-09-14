package com.rey.dumbdumb.domain.usecase.repository

import com.rey.lib.cleanarch.domain.dto.Result

internal interface IAuthRepository {
    suspend fun authenticate(username: String, password: String): Result<String>
}