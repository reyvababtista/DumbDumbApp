package com.rey.dumbdumb.external.di.authentication.module

import com.rey.dumbdumb.data.repository.FakeAuthRepository
import com.rey.dumbdumb.data.repository.SecureRepository
import com.rey.dumbdumb.data.repository.source.local.ISecureData
import com.rey.dumbdumb.data.repository.source.local.ISecureLocalData
import com.rey.dumbdumb.data.source.local.SecureData
import com.rey.dumbdumb.data.source.local.SecureLocalData
import com.rey.dumbdumb.domain.usecase.repository.IAuthRepository
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindSecureRepository(repository: SecureRepository): ISecureRepository

    @Binds
    abstract fun bindSecureData(data: SecureData): ISecureData

    @Binds
    abstract fun bindSecureLocalData(data: SecureLocalData): ISecureLocalData

    @Binds
    abstract fun bindAuthRepository(data: FakeAuthRepository): IAuthRepository
}