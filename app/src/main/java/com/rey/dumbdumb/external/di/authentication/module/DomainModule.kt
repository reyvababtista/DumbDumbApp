package com.rey.dumbdumb.external.di.authentication.module

import com.rey.dumbdumb.domain.entity.usecase.IAuthUseCase
import com.rey.dumbdumb.domain.entity.usecase.ISecureUseCase
import com.rey.dumbdumb.domain.usecase.AuthUseCase
import com.rey.dumbdumb.domain.usecase.SecureUseCase
import dagger.Binds
import dagger.Module

@Module
internal abstract class DomainModule {
    @Binds
    abstract fun bindSecureUseCase(useCase: SecureUseCase): ISecureUseCase

    @Binds
    abstract fun bindAuthUseCase(useCase: AuthUseCase): IAuthUseCase
}