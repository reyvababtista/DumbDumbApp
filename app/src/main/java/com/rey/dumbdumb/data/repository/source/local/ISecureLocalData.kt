package com.rey.dumbdumb.data.repository.source.local

import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.lib.cleanarch.domain.dto.Result

internal interface ISecureLocalData {
    suspend fun storeCredential(data: EncryptReq): Result<Unit>
    suspend fun getCredential(): Result<EncryptRes>
}