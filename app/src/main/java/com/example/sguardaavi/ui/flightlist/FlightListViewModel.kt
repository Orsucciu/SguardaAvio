package com.example.sguardaavi.ui.flightlist

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.sguardaavi.data.Flight
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import java.util.*

class FlightListViewModel(application: Application) :
    AndroidViewModel(application) {
    var flightListLiveData =
        MutableLiveData<List<Flight>>()

    fun loadData(
        begin: Long,
        end: Long,
        isArrival: Boolean,
        icao: String?
    ) { // Instantiate the RequestQueue.
        val queue =
            Volley.newRequestQueue(getApplication<Application>().applicationContext)
        val urlBuilder =
            StringBuilder("https://opensky-network.org/api/flights/")
        urlBuilder.append(if (isArrival) "arrival" else "departure").append("?").append("airport=")
            .append(icao).append("&begin=").append(begin).append("&end=")
            .append(end)
        Log.i(TAG, "URL is $urlBuilder")
        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET,
            urlBuilder.toString(),
            Response.Listener { response ->
                Log.i(
                    TAG,
                    "String response is $response"
                )
                val flightList: MutableList<Flight> =
                    ArrayList()
                val gson = Gson()
                val flightsJsonArray = getFlightsRequestJson(response)
                for (flightObject in flightsJsonArray) {
                    flightList.add(
                        gson.fromJson(
                            flightObject.asJsonObject,
                            Flight::class.java
                        )
                    )
                }
                Log.i(
                    TAG,
                    "flight list has size" + flightList.size
                )
                flightListLiveData.value = flightList
            },
            Response.ErrorListener { })
        // Add the request to the RequestQueue.
        queue.run {
            // Add the request to the RequestQueue.
            add(stringRequest)
        }
    }

    private fun getFlightsRequestJson(jsonString: String): JsonArray {
        val parser = JsonParser()
        val jsonElement = parser.parse(jsonString)
        return jsonElement.asJsonArray
    }

    companion object {
        private val TAG = FlightListViewModel::class.java.simpleName
    }
}