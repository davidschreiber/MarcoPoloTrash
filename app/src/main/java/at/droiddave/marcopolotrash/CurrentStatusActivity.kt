package at.droiddave.marcopolotrash

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import at.droiddave.marcopolotrash.databinding.ActivityCurrentStatusBinding

/**
 * Shows the current status of the trash chute (open/closed).
 */
class CurrentStatusActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, CurrentStatusActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityCurrentStatusBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_status)
    }
}
