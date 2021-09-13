package com.rey.dumbdumb.external.di.authentication.module

import com.rey.dumbdumb.presentation.fragment.AuthenticationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthenticationFragmentsModule {

    @ContributesAndroidInjector
    abstract fun contributeAuthenticationFragment(): AuthenticationFragment
}