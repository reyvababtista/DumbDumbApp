package com.rey.dumbdumb.data.repository.source.local

internal interface ISecureData {
    suspend fun generateSecretKey()
}