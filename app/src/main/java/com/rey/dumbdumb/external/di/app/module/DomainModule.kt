package com.rey.dumbdumb.external.di.app.module

import com.rey.lib.cleanarch.domain.usecase.external.ICoroutineProvider
import com.rey.lib.cleanarch.external.CoroutineProvider
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @Provides
    fun provideCoroutineProvider(): ICoroutineProvider = CoroutineProvider()
}