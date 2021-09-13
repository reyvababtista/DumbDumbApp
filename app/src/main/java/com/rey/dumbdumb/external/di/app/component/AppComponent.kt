package com.rey.dumbdumb.external.di.app.component

import com.rey.dumbdumb.DumbDumbApp
import com.rey.dumbdumb.external.di.app.module.DomainModule
import com.rey.dumbdumb.external.di.app.module.PresentationModule
import com.rey.dumbdumb.external.di.authentication.module.AuthenticationActivityModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AuthenticationActivityModule::class,
        DomainModule::class,
        PresentationModule::class
    ]
)
interface AppComponent : AndroidInjector<DumbDumbApp>