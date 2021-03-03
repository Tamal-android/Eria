package com.magicmind.eria.di.module

import androidx.lifecycle.ViewModelProvider
import com.magicmind.eria.di.viewmodel.ViewModelProvidersFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactory: ViewModelProvidersFactory): ViewModelProvider.Factory
}