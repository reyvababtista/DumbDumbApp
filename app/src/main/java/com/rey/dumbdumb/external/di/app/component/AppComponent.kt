package com.rey.dumbdumb.external.di.app.component

import android.content.Context
import com.rey.dumbdumb.DumbDumbApp
import com.rey.dumbdumb.external.di.app.annotation.ApplicationContext
import com.rey.dumbdumb.external.di.app.module.DataModule
import com.rey.dumbdumb.external.di.app.module.DomainModule
import com.rey.dumbdumb.external.di.app.module.PresentationModule
import com.rey.dumbdumb.external.di.authentication.module.AuthenticationActivityModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AuthenticationActivityModule::class,
        DataModule::class,
        DomainModule::class,
        PresentationModule::class
    ]
)
interface AppComponent : AndroidInjector<DumbDumbApp> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun context(@ApplicationContext context: Context): Builder
        fun build(): AppComponent
    }
}