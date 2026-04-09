package com.example.sagareduapp1.viewmodel

import android.app.Application
import com.example.sagareduapp1.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class EduApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@EduApplication)
            modules(appModule)
        }
    }
}
