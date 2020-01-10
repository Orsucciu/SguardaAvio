package com.example.sguardaavi

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import com.example.sguardaavi.ui.flightdetails.FlightDetailsFragment
import androidx.appcompat.app.AppCompatActivity

class FlightDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.global_activity)

        val intent = intent

        val firstseen = intent.getLongExtra(FIRSTSEEN, -1)
        val icao = intent.getStringExtra(ICAO)

        val flFragment = FlightDetailsFragment.newInstance(firstseen, icao)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, flFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    companion object {
        private val ICAO = "icao"
        private val FIRSTSEEN = "firstseen"

        fun startActivity(
            activity: Activity,
            firstseen: Long,
            icao: String
        ) {

            val i = Intent(activity, GlobalActivity::class.java)
            i.putExtra(ICAO, icao)
            i.putExtra(FIRSTSEEN, firstseen)
            activity.startActivity(i)
        }
    }
}