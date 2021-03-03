package com.magicmind.eria.di

import com.magicmind.eria.app.EriaApplication
import com.magicmind.eria.di.module.ApiModule
import com.magicmind.eria.di.module.AppActivityModuleBuilder
import com.magicmind.eria.di.module.SharedPrefModule
import com.magicmind.eria.di.module.ViewModelFactoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        AppActivityModuleBuilder::class,
        ApiModule::class,
        ViewModelFactoryModule::class,
        SharedPrefModule::class
    ]
)
interface AppComponent : AndroidInjector<EriaApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: EriaApplication): Builder

        fun build(): AppComponent
    }
}