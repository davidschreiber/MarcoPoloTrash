package at.droiddave.marcopolotrash

import org.threeten.bp.LocalTime


/**
 * The `MaintenanceTime` describes a time span between a start and end time where the trash chute is being cleaned and thus out of order.
 * Start and end times are inclusive.
 */
data class MaintenanceTime(val from: LocalTime, val until: LocalTime)

/**
 * Public interface of a time table holding trash chute suction times. During the suction time, the trash chute is closed.
 */
interface MaintenanceTimeTable {
    /**
     * Returns the next known maintenance time during which the chute is out of order. If right now there is an ongoing maintenance,
     * the current maintenance time is returned.
     */
    fun getNextMaintenanceTime(): MaintenanceTime
}


class MaintenanceTimeTableImpl(private val timeProvider: TimeProvider) : MaintenanceTimeTable {
    override fun getNextMaintenanceTime(): MaintenanceTime {
        val now = timeProvider.getCurrentTime()
        return maintenanceTimes.find { it.until.isAfter(now) } ?: maintenanceTimes.first()
    }
}

private val maintenanceTimes = listOf(
    MaintenanceTime(from = time(8, 0), until = time(8, 30)),
    MaintenanceTime(from = time(10, 0), until = time(10, 30)),
    MaintenanceTime(from = time(12, 0), until = time(12, 30)),
    MaintenanceTime(from = time(14, 0), until = time(14, 30)),
    MaintenanceTime(from = time(16, 0), until = time(16, 30))
)

private fun time(hour: Int, minute: Int) = LocalTime.of(hour, minute)


