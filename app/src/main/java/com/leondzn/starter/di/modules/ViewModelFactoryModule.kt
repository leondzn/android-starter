package com.leondzn.starter.di.modules

import android.arch.lifecycle.ViewModelProvider
import com.leondzn.starter.ViewModelFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory
}