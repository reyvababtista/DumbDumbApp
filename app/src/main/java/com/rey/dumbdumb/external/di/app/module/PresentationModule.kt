package com.rey.dumbdumb.external.di.app.module

import androidx.lifecycle.ViewModelProvider
import com.rey.dumbdumb.presentation.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
internal abstract class PresentationModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}