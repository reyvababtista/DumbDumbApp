package com.rey.dumbdumb.domain.dto

internal data class EncryptRes(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
)
