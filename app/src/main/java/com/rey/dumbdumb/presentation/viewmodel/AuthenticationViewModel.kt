package com.rey.dumbdumb.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rey.dumbdumb.domain.entity.usecase.ISecureUseCase
import com.rey.lib.cleanarch.domain.dto.onError
import com.rey.lib.cleanarch.domain.dto.onSuccess
import com.rey.lib.cleanarch.domain.usecase.external.ICoroutineProvider
import kotlinx.coroutines.launch
import javax.crypto.Cipher
import javax.inject.Inject

internal class AuthenticationViewModel @Inject constructor(
    private val coroutine: ICoroutineProvider,
    private val useCase: ISecureUseCase
) : ViewModel() {

    private val _cipher = MutableLiveData<Cipher>()
    val cipher: LiveData<Cipher> = _cipher

    fun getChiper() = viewModelScope.launch(coroutine.default()) {
        useCase.generateCipher()
            .onSuccess {
                _cipher.postValue(it)
            }.onError {
                Log.e("MY_APP_TAG", "getChiper: ${it}")
            }
    }
}