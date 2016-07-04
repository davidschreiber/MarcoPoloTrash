package at.droiddave.marcopolotrash.internal

import org.threeten.bp.LocalTime

interface Presenter<in V> {
    fun bindView(view: V)
    fun detachView()
}

interface TimeProvider {
    fun getCurrentTime(): LocalTime
}

internal class RealTimeProvider() : TimeProvider {
    override fun getCurrentTime(): LocalTime {
        return LocalTime.now()
    }
}