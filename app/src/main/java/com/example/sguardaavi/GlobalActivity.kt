package com.example.sguardaavi

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import android.widget.Toast
import android.os.AsyncTask
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import java.net.URL
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.sguardaavi.ui.flightlist.FlightListFragment
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.nio.charset.Charset


class GlobalActivity : AppCompatActivity() {

    private var task : AsyncTask<Void, Void, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.global_activity)

        val intent = intent

        val begin = intent.getLongExtra(BEGIN, -1)//.toString()
        val end = intent.getLongExtra(END, -1)//.toString()
        val isArrival = intent.getBooleanExtra(IS_ARRIVAL, false)
        val icao = intent.getStringExtra(ICAO)

        /*
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                FlightListFragment.newInstance(begin, end, isArrival, icao)
            ).commitNow()
        }
        */

        task = object : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void): String {
                val apiUrl = StringBuilder("https://opensky-network.org/api/flights/")

                if (isArrival) {
                    apiUrl.append("arrival")
                } else {
                    apiUrl.append("departure")
                }

                val params = mapOf(
                    "airport" to icao,
                    "begin" to begin.toString(),
                    "end" to end.toString()
                ) as Map<String, String>

                val resultJson = requestFromServer(apiUrl.toString(), params)

                Log.i("RESULT", resultJson)

                Log.i("RESULT-OBJ", Utils.getFlightList(resultJson).toString())

                //Utils.getFlightList(resultJson).get(0)

                return resultJson
            }

            override fun onPostExecute(s: String) {
                if (!isFinishing && !isCancelled) {
                    Log.d("Request", s)
                    //Toast.makeText(this, "Request performed", Toast.LENGTH_LONG).show()
                }
            }
        }.execute()

        val flFragment = FlightListFragment.newInstance(begin, end, isArrival, icao)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, flFragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    override fun onDestroy() {
        super.onDestroy()

        task?.cancel(true)
        task = null
    }

    fun requestFromServer(apiUrl : String, getParams : Map<String, String>) : String {

        var urlConnection : HttpURLConnection? = null

        try {

            val makeApiUrl = StringBuilder(apiUrl)
            makeApiUrl.append('?')
            makeApiUrl.append("airport")
            makeApiUrl.append('=')
            makeApiUrl.append(getParams.get("airport"))
            makeApiUrl.append('&')
            makeApiUrl.append("begin")
            makeApiUrl.append('=')
            makeApiUrl.append(getParams.get("begin"))
            makeApiUrl.append('&')
            makeApiUrl.append("end")
            makeApiUrl.append('=')
            makeApiUrl.append(getParams.get("end"))
            /*
            if (!getParams.isEmpty()) {
                makeApiUrl.append('?')
                for ((k, v) in getParams) {
                    makeApiUrl.append(k)
                    makeApiUrl.append('=')
                    makeApiUrl.append(v)
                    makeApiUrl.append('&')
                }
            }
            */

            Log.i("REQUEST-URL", makeApiUrl.toString())

            val url = URL(makeApiUrl.toString())
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.setRequestMethod("GET")
            urlConnection.connect()
            val inputStream = urlConnection.getInputStream()

            val content = inputStream.readBytes().toString(Charset.defaultCharset())
            return content

        } catch (e: IOException) {
            Log.e("Request", "Error ", e)
        } finally {
            urlConnection?.disconnect()
        }

        return "[]"
    }

    companion object {
        private val BEGIN = "begin"
        private val END = "end"
        private val IS_ARRIVAL = "isArrival"
        private val ICAO = "icao"

        fun startActivity(
            activity: Activity,
            begin: Long,
            end: Long,
            isArrival: Boolean,
            icao: String
        ) {

            val i = Intent(activity, GlobalActivity::class.java)
            i.putExtra(BEGIN, begin)
            i.putExtra(END, end)
            i.putExtra(IS_ARRIVAL, isArrival)
            i.putExtra(ICAO, icao)
            activity.startActivity(i)
        }
    }

    fun getFlights(airportName : String, isDeparture : Boolean, fromDate : String, toDate : String) : List<Flight> {
        return ArrayList<Flight>()
    }
}
