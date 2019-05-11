package me.ricardo.romero.fersi

import android.app.Application
import me.ricardo.romero.fersi.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class FersiApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@FersiApp)
            modules(appModule)
        }
    }
}