package at.droiddave.marcopolotrash

import at.droiddave.marcopolotrash.internal.MaintenanceTimeTable
import at.droiddave.marcopolotrash.internal.MaintenanceWindow
import at.droiddave.marcopolotrash.internal.TimeProvider
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import org.jetbrains.spek.api.Spek
import org.mockito.Matchers.anyLong
import org.mockito.Mockito
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.threeten.bp.LocalTime

var injectedTime: LocalTime = LocalTime.of(12, 0)
var injectedMaintenanceWindow: MaintenanceWindow = MaintenanceWindow(LocalTime.of(0, 0), LocalTime.of(0, 0))

private val mockKodein = Kodein {
    bind<TimeProvider>() with instance(object : TimeProvider {
        override fun getCurrentTime() = injectedTime
    })

    bind<MaintenanceTimeTable>() with instance(object : MaintenanceTimeTable {
        override val maintenanceWindows: Iterator<MaintenanceWindow> get() = listOf(injectedMaintenanceWindow).iterator()
        override fun getNextMaintenanceWindow() = injectedMaintenanceWindow
    })
}


inline fun <reified T : Any> mock(): T = Mockito.mock(T::class.java)

/**
 * Example local unit test, which will execute on the development machine (host).

 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class MaintenanceStatusTest : Spek({
    given("A CurrentStatusPresenter") {
        val presenter: MaintenanceStatusPresenter = MaintenanceStatusPresenter { mockKodein }
        var mockView: MaintenanceStatusContract.View = mock()

        beforeEach {
            mockView = mock()
        }

        afterEach {
            presenter.detachView()
        }

        context("current time is 12:00") {
            beforeEach { injectedTime = LocalTime.of(12, 0) }

            context("next maintenanceWindow is 12:30-13:00") {
                beforeEach { injectedMaintenanceWindow = MaintenanceWindow(LocalTime.of(12, 30), LocalTime.of(13, 0)) }

                on("calling bindView") {
                    beforeEach { presenter.bindView(mockView) }

                    it("will show the chute being open for 30 more minutes") {
                        verify(mockView).showOpen(30)
                    }

                    it("will not call #showClosed on the view") {
                        verify(mockView, never()).showClosed(anyLong())
                    }
                }
            }

            context("next maintenance window is 12:00-12:45") {
                beforeEach { injectedMaintenanceWindow = MaintenanceWindow(LocalTime.of(12, 0), LocalTime.of(12, 45)) }

                on("calling bindView") {
                    beforeEach { presenter.bindView(mockView) }

                    it("will show chute as closed for 45 more minutes") {
                        verify(mockView).showClosed(45)
                    }
                }
            }

            context("next maintenance window is 11:45-12:00") {
                beforeEach { injectedMaintenanceWindow = MaintenanceWindow(LocalTime.of(11, 45), LocalTime.of(12, 0)) }

                on("calling bindView") {
                    beforeEach { presenter.bindView(mockView) }

                    it("will show closed for another 0 minutes") {
                        verify(mockView).showClosed(0)
                    }
                }
            }

            context("next maintenance window is tomorrow 11:00-11:30") {
                beforeEach { injectedMaintenanceWindow = MaintenanceWindow(LocalTime.of(11, 0), LocalTime.of(11, 30)) }

                on("calling bindView") {
                    beforeEach { presenter.bindView(mockView) }

                    it("will show open for another 1380 minutes") {
                        verify(mockView).showOpen(1380)
                    }
                }
            }
        }
    }
})