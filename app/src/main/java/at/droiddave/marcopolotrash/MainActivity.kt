package at.droiddave.marcopolotrash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.github.salomonbrys.kodein.android.appKodein
import com.github.salomonbrys.kodein.lazyInstance

class MainActivity : AppCompatActivity() {

    val timeProvider by appKodein.lazyInstance<at.droiddave.marcopolotrash.internal.TimeProvider>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "The time is ${timeProvider.getCurrentTime()}", Toast.LENGTH_LONG).show()

        CurrentStatusActivity.start(this)

    }
}