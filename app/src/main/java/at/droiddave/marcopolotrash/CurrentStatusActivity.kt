package at.droiddave.marcopolotrash

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import at.droiddave.marcopolotrash.databinding.ActivityCurrentStatusBinding
import at.droiddave.marcopolotrash.internal.MaintenanceTimeTable
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.lazyInstance

/**
 * Shows the current status of the trash chute (open/closed).
 */
class CurrentStatusActivity : AppCompatActivity() {

    private val maintenanceTimes: MaintenanceTimeTable by appKodein.lazyInstance()

    private lateinit var binding: ActivityCurrentStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_status)
        maintenanceTimes.getNextMaintenanceTime()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CurrentStatusActivity::class.java)
            context.startActivity(intent)
        }
    }
}


