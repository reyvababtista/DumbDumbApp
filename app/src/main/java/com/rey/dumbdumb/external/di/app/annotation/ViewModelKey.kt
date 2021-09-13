package com.rey.dumbdumb.external.di.app.annotation

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

@MapKey
annotation class ViewModelKey(val qualifier: KClass<out ViewModel>)
