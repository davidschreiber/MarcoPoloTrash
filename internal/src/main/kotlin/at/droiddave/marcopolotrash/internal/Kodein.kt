package at.droiddave.marcopolotrash.internal

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.singleton

val appDependencies = Kodein {
    bind<TimeProvider>() with singleton { RealTimeProvider() }
    bind<MaintenanceTimeTable>() with singleton { MaintenanceTimeTableImpl(instance()) }
}