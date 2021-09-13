package com.rey.dumbdumb.external.di.authentication.module

import com.rey.dumbdumb.external.di.app.annotation.ActivityScope
import com.rey.dumbdumb.presentation.activity.AuthenticationActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthenticationActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            AuthenticationFragmentsModule::class,
            RepositoryModule::class,
            DomainModule::class,
            PresentationModule::class
        ]
    )
    abstract fun contributeAuthenticationActivity(): AuthenticationActivity
}