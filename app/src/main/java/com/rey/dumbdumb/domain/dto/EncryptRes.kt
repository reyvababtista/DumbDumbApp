package com.rey.dumbdumb.domain.dto

data class EncryptRes(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
)
