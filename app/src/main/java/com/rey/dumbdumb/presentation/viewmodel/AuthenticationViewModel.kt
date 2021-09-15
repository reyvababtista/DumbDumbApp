package com.rey.dumbdumb.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rey.dumbdumb.DumbDumbApp.Companion.TAG
import com.rey.dumbdumb.domain.dto.EncryptReq
import com.rey.dumbdumb.domain.dto.EncryptRes
import com.rey.dumbdumb.domain.entity.usecase.IAuthUseCase
import com.rey.lib.cleanarch.domain.dto.onError
import com.rey.lib.cleanarch.domain.dto.onSuccess
import com.rey.lib.cleanarch.domain.usecase.external.ICoroutineProvider
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

internal class AuthenticationViewModel @Inject constructor(
    private val coroutine: ICoroutineProvider,
    private val useCase: IAuthUseCase
) : ViewModel() {

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> = _token

    fun authenticate(username: String, password: String) = viewModelScope.launch(coroutine.io()) {
        useCase.authenticate(username, password)
            .onSuccess { _token.postValue(it) }
            .onError {
                _token.postValue(null)
                Log.e(TAG, "authenticate(username: String, password: String): $it")
            }
    }

    private val _encryptionCipher = MutableLiveData<Cipher>()
    val encryptionCipher: LiveData<Cipher> = _encryptionCipher

    fun getEncryptionCipher() = viewModelScope.launch(coroutine.default()) {
        useCase.getEncryptionCipher()
            .onSuccess { _encryptionCipher.postValue(it) }
            .onError { Log.e(TAG, "getEncryptionCipher(): $it") }
    }

    private val _encryptedCredential = MutableLiveData<EncryptRes?>()
    val encryptedCredential: LiveData<EncryptRes?> = _encryptedCredential

    fun getEncryptedCredential() = viewModelScope.launch(coroutine.io()) {
        useCase.getEncryptedCredential()
            .onSuccess { _encryptedCredential.postValue(it) }
            .onError {
                _encryptedCredential.postValue(null)
                Log.e(TAG, "getEncryptedCredential(): $it")
            }
    }

    private val _decryptionCipher = MutableLiveData<Cipher>()
    val decryptionCipher: LiveData<Cipher> = _decryptionCipher

    fun getDecryptionCipher(initializationVector: ByteArray) =
        viewModelScope.launch(coroutine.default()) {
            useCase.getDecryptionCipher(initializationVector)
                .onSuccess { _decryptionCipher.postValue(it) }
                .onError { Log.e(TAG, "getDecryptionCipher(initializationVector: ByteArray): $it") }
        }

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    fun authenticate() = viewModelScope.launch(coroutine.io()) {
        useCase.authenticate(
            encryptionCipher = encryptionCipher.value,
            decryptionCipher = decryptionCipher.value,
            token = token.value,
            encryptedCredential = encryptedCredential.value?.let {
                EncryptReq(it.ciphertext, it.initializationVector)
            }
        )
            .onSuccess { _name.postValue(it) }
            .onError { Log.e(TAG, "authenticate(): $it") }
    }
}