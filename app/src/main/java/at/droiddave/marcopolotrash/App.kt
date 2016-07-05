package at.droiddave.marcopolotrash

import android.app.Application
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.android.KodeinApplication
import com.github.salomonbrys.kodein.singleton
import com.jakewharton.threetenabp.AndroidThreeTen

class App : Application(), KodeinApplication {
    override val kodein = Kodein {
        bind<TimeProvider>() with singleton { RealTimeProvider() }
        bind<MaintenanceTimeTable>() with singleton { MaintenanceTimeTableImpl(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
