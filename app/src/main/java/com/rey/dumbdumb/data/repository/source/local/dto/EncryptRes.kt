package com.rey.dumbdumb.data.repository.source.local.dto

internal data class EncryptRes(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
)
