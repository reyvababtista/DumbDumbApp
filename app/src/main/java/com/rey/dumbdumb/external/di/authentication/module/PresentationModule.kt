package com.rey.dumbdumb.external.di.authentication.module

import androidx.lifecycle.ViewModel
import com.rey.dumbdumb.domain.entity.usecase.ISecureUseCase
import com.rey.dumbdumb.external.di.app.annotation.ViewModelKey
import com.rey.dumbdumb.presentation.viewmodel.AuthenticationViewModel
import com.rey.lib.cleanarch.domain.usecase.external.ICoroutineProvider
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class PresentationModule {

    @Provides
    @IntoMap
    @ViewModelKey(AuthenticationViewModel::class)
    fun bindAuthenticationViewModel(
        coroutine: ICoroutineProvider,
        useCase: ISecureUseCase
    ): ViewModel = AuthenticationViewModel(coroutine, useCase)
}