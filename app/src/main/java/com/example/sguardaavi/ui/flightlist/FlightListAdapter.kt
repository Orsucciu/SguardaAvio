package com.example.sguardaavi.ui.flightlist

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sguardaavi.FlightDetailsActivity
import com.example.sguardaavi.R
import com.example.sguardaavi.Utils
import com.example.sguardaavi.data.Flight
import com.example.sguardaavi.ui.flightlist.FlightListAdapter.FlightViewHolder
import java.util.*


class FlightListAdapter : RecyclerView.Adapter<FlightViewHolder>() {
    private var mFlightsList: MutableList<Flight> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.flight_list_item, parent, false)
        return FlightViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        Log.i(TAG, mFlightsList[position].callsign)
        holder.callSignView!!.text = mFlightsList[position].callsign

        if (mFlightsList[position].estArrivalAirport == null) {
            holder.estArrivalAirport!!.text = "Err"
        } else {
            holder.estArrivalAirport!!.text = (mFlightsList[position].estArrivalAirport);
        }

        if (mFlightsList[position].estDepartureAirport == null) {
            holder.estDepartureAirport!!.text = "Err"
        } else {
            holder.estDepartureAirport!!.text = (mFlightsList[position].estDepartureAirport);
        }

        var firstseen = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(mFlightsList[position].firstSeen))
        var lastseen = java.time.format.DateTimeFormatter.ISO_INSTANT
            .format(java.time.Instant.ofEpochSecond(mFlightsList[position].lastSeen))


        holder.callSignView!!.text = (mFlightsList[position].callsign);
        holder.firstSeen!!.text = firstseen.toString()
        holder.lastSeen!!.text = lastseen.toString()
        holder.flightTime!!.text = java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(mFlightsList[position].lastSeen - mFlightsList[position].firstSeen))


        holder.itemView.setOnClickListener { v ->
            val context = v.context
            val intent = Intent(context, FlightDetailsActivity::class.java)
            intent.putExtra("icao", mFlightsList[position].icao24)
            intent.putExtra("estArrivalAirport", mFlightsList[position].estArrivalAirport)
            intent.putExtra("estDepartureAirport", mFlightsList[position].estDepartureAirport)
            intent.putExtra(
                "flightTime",
                java.time.format.DateTimeFormatter.ISO_INSTANT.format(java.time.Instant.ofEpochSecond(mFlightsList[position].lastSeen - mFlightsList[position].firstSeen))

            )
            intent.putExtra("firstSeen", mFlightsList[position].firstSeen)
            intent.putExtra("lastSeen", mFlightsList[position].lastSeen)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        Log.i(TAG, "" + mFlightsList.size)
        return mFlightsList.size
    }

    fun setFlights(flightsList: Collection<Flight>) {
        mFlightsList.clear()
        mFlightsList.addAll(flightsList)
        notifyDataSetChanged()
    }

    inner class FlightViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var callSignView: TextView? = null
        var estDepartureAirport: TextView? = null
        var estArrivalAirport: TextView? = null
        var firstSeen: TextView? = null
        var lastSeen: TextView? = null
        var flightTime: TextView? = null

        init {
            this.callSignView = itemView.findViewById(R.id.flight_name)
            this.estArrivalAirport = itemView.findViewById(R.id.arrival_airport)
            this.estDepartureAirport = itemView.findViewById(R.id.departure_airport)
            this.firstSeen = itemView.findViewById(R.id.departure_time)
            this.lastSeen = itemView.findViewById(R.id.arrival_time)
            this.flightTime = itemView.findViewById(R.id.flight_time)
        }
    }

    companion object {
        private val TAG = FlightListAdapter::class.java.simpleName
    }
}