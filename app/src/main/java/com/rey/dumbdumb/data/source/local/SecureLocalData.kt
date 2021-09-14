package com.rey.dumbdumb.data.source.local

import com.google.gson.Gson
import com.rey.dumbdumb.data.repository.source.local.ISecureLocalData
import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.lib.cleanarch.data.source.local.SharedPrefs
import com.rey.lib.cleanarch.domain.dto.Result
import com.rey.lib.cleanarch.domain.dto.suspendTryCatch

internal class SecureLocalData(private val gson: Gson, private val pref: SharedPrefs) :
    ISecureLocalData {

    override suspend fun storeCredential(data: EncryptReq): Result<Unit> = suspendTryCatch {
        val encryptedCred = gson.toJson(data)
        pref.set(CREDENTIAL_PREF_KEY, encryptedCred)
        Result.Success(Unit)
    }

    override suspend fun getCredential(): Result<EncryptRes> = suspendTryCatch {
        val data = pref.getPref().getString(CREDENTIAL_PREF_KEY, "")
        val encryptedCred = gson.fromJson(data, EncryptRes::class.java)
        Result.Success(encryptedCred)
    }

    companion object {
        private const val CREDENTIAL_PREF_KEY = "encryptedTokenCredential"
    }
}