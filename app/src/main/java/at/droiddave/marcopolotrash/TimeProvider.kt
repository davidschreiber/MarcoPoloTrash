package at.droiddave.marcopolotrash

import org.threeten.bp.LocalTime


interface TimeProvider {
    fun getCurrentTime(): LocalTime
}

class RealTimeProvider() : TimeProvider {
    override fun getCurrentTime(): LocalTime {
        return LocalTime.now()
    }
}