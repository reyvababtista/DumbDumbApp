package com.rey.dumbdumb

import com.rey.dumbdumb.external.di.app.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class DumbDumbApp : DaggerApplication() {

    companion object {
        const val TAG = "DumbDumbTag"
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> =
        DaggerAppComponent.builder().context(this).build()
}