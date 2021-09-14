package com.rey.dumbdumb.domain.dto

data class EncryptReq(
    val ciphertext: ByteArray,
    val initializationVector: ByteArray
)
