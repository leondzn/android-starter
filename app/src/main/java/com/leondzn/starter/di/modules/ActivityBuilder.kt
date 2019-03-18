package com.leondzn.starter.di.modules

import dagger.Module

@Module(includes = [ViewModelFactoryModule::class, ViewModelModule::class])
abstract class ActivityBuilder
