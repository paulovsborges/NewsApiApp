package com.pvsb.newsapiapp.di

import android.app.Application
import com.pvsb.newsapiapp.di.modules.networkModule
import com.pvsb.newsapiapp.di.modules.repositoryModule
import com.pvsb.newsapiapp.di.modules.viewModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp:Application() {

    override fun onCreate() {
        super.onCreate()
        initiateKoin()
    }

    private fun initiateKoin() {
        startKoin {
            androidContext(this@MyApp)
            androidLogger()
            modules(
                viewModule,
                repositoryModule,
                networkModule
            )
        }
    }
}
