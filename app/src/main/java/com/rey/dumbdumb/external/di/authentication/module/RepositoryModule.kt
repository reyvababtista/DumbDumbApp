package com.rey.dumbdumb.external.di.authentication.module

import com.rey.dumbdumb.data.repository.SecureRepository
import com.rey.dumbdumb.data.repository.source.local.ISecureData
import com.rey.dumbdumb.data.source.local.SecureData
import com.rey.dumbdumb.domain.usecase.repository.ISecureRepository
import dagger.Binds
import dagger.Module

@Module
internal abstract class RepositoryModule {

    @Binds
    abstract fun bindSecureRepository(repository: SecureRepository): ISecureRepository

    @Binds
    abstract fun bindSecureData(data: SecureData): ISecureData
}