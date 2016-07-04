package at.droiddave.marcopolotrash

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import at.droiddave.marcopolotrash.databinding.ActivityCurrentStatusBinding
import at.droiddave.marcopolotrash.internal.*
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.lazyInstance
import org.threeten.bp.LocalTime
import org.threeten.bp.LocalTime.MIDNIGHT
import org.threeten.bp.temporal.ChronoUnit.MINUTES

interface MaintenanceStatusContract {
    interface Presenter : at.droiddave.marcopolotrash.internal.Presenter<View>
    interface View {
        fun showClosed(remainingMinutes: Long)
        fun showOpen(remainingMinutes: Long)
    }
}

class MaintenanceStatusPresenter(kodein: () -> Kodein) : MaintenanceStatusContract.Presenter {
    private val maintenanceTimes: MaintenanceTimeTable by kodein.lazyInstance()
    private val timeProvider: TimeProvider by kodein.lazyInstance()

    private var boundView: MaintenanceStatusContract.View? = null

    override fun bindView(view: MaintenanceStatusContract.View) {
        boundView = view
        showMaintenanceStatus(view)
    }

    override fun detachView() {
        boundView = null
    }

    private fun showMaintenanceStatus(view: MaintenanceStatusContract.View) {
        val nextMaintenanceTime = maintenanceTimes.getNextMaintenanceWindow()

        if (nextMaintenanceTime.isNow()) {
            view.showClosed(remainingMinutes = minutesUntil(nextMaintenanceTime.end))
        } else {
            view.showOpen(remainingMinutes = minutesUntil(nextMaintenanceTime.from))
        }
    }

    private fun MaintenanceWindow.isNow() = this.contains(timeProvider.getCurrentTime())
    private fun minutesUntil(time: LocalTime): Long {
        val now = timeProvider.getCurrentTime()

        // If the given time is earlier in the day, calculate minutes until the time next day.
        // MIDNIGHT is defined as 00:00 so calculating minutes from now to midnight is a negative number. +
        // Instead count the minutes to the maximum possible time 23:59 and add 1.
        return when {
            now <= time -> now.until(time, MINUTES)
            else -> now.until(LocalTime.MAX, MINUTES) + 1 + MIDNIGHT.until(time, MINUTES)
        }
    }
}

/**
 * Shows the current status of the trash chute (open/closed).
 */
class CurrentStatusActivity : AppCompatActivity(), MaintenanceStatusContract.View {
    private lateinit var binding: ActivityCurrentStatusBinding

    // TODO Make the presenter injectable, so we can actually test UI only.
    private val presenter = MaintenanceStatusPresenter(appKodein)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_status)
        presenter.bindView(this)
    }

    override fun showClosed(remainingMinutes: Long) {
        binding.apply {
            activityCurrentStatus.setBackgroundColor(getColorCompat(R.color.statusChuteClosed))
            mainStatusMessage.setText(R.string.trash_chute_is_closed)
            secondaryStatusMessage.text = getString(R.string.opens_up_in_minutes, remainingMinutes)
            statusIcon.setImageResource(R.drawable.ic_cross)
        }
    }

    override fun showOpen(remainingMinutes: Long) {
        binding.apply {
            activityCurrentStatus.setBackgroundColor(getColorCompat(R.color.statusChuteOpen))
            mainStatusMessage.setText(R.string.trash_chute_is_open)
            secondaryStatusMessage.text = getString(R.string.still_open_for_minutes, remainingMinutes)
            statusIcon.setImageResource(R.drawable.ic_check)
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CurrentStatusActivity::class.java)
            context.startActivity(intent)
        }
    }
}