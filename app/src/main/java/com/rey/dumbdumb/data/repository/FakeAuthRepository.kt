package com.rey.dumbdumb.data.repository

import com.rey.dumbdumb.domain.usecase.repository.IAuthRepository
import com.rey.lib.cleanarch.domain.dto.Result
import javax.inject.Inject

internal class FakeAuthRepository @Inject constructor() : IAuthRepository {
    override suspend fun authenticate(username: String, password: String): Result<String> =
        Result.Success(FAKE_TOKEN)

    companion object {
        private const val FAKE_TOKEN =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkZvbyBCYXIiLCJpYXQiOjE1MTYyMzkwMjJ9.ofV_rETulkW5agRmGt4wRs8QsU8WTdqDA3xjIaK4Yn8"
    }
}