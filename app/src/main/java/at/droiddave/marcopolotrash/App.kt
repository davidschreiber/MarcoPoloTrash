package at.droiddave.marcopolotrash

import android.app.Application
import at.droiddave.marcopolotrash.internal.appDependencies
import com.github.salomonbrys.kodein.android.KodeinApplication
import com.jakewharton.threetenabp.AndroidThreeTen

class App : Application(), KodeinApplication {
    override val kodein = appDependencies

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
