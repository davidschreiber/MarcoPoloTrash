package at.droiddave.marcopolotrash.internal

import org.threeten.bp.LocalTime

/**
 * The `MaintenanceWindow` describes a time span between a start and end time where the trash chute is being cleaned and thus out of order.
 * Start and end times are inclusive.
 */
data class MaintenanceWindow(val from: LocalTime, val end: LocalTime)

/**
 * Returns `true` if the [time] is inside this maintenance time. Start and end times are
 */
fun MaintenanceWindow.contains(time: LocalTime): Boolean {
    return time >= this.from && time <= this.end
}



/**
 * Public interface of a time table holding trash chute suction times. During the suction time, the trash chute is closed.
 */
interface MaintenanceTimeTable {
    /**
     * Iterator giving access to trash chute maintenance windows.
     */
    val maintenanceWindows: Iterator<MaintenanceWindow>

    /**
     * Returns the next known maintenance time during which the chute is out of order. If right now there is an ongoing maintenance,
     * the current maintenance time is returned.
     */
    fun getNextMaintenanceWindow(): MaintenanceWindow
}

/**
 * Inject the required [TimeProvider] via Kodein.
 */
internal class MaintenanceTimeTableImpl(private val timeProvider: TimeProvider) : MaintenanceTimeTable {
    override val maintenanceWindows: Iterator<MaintenanceWindow>
        get() = maintenanceTimeList.iterator()

    override fun getNextMaintenanceWindow(): MaintenanceWindow {
        val now = timeProvider.getCurrentTime()
        return maintenanceTimeList.find { it.end.isAfter(now) } ?: maintenanceTimeList.first()
    }

    private fun time(hour: Int, minute: Int) = LocalTime.of(hour, minute)
    private val maintenanceTimeList = listOf(
        MaintenanceWindow(from = time(8, 0), end = time(8, 30)),
        MaintenanceWindow(from = time(10, 0), end = time(10, 30)),
        MaintenanceWindow(from = time(12, 0), end = time(12, 30)),
        MaintenanceWindow(from = time(14, 0), end = time(14, 30)),
        MaintenanceWindow(from = time(16, 0), end = time(16, 30))
    )
}