package com.example.sguardaavi.ui.flightlist

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sguardaavi.data.Flight
import com.example.sguardaavi.ui.flightlist.FlightListAdapter.FlightViewHolder
import java.util.*

class FlightListAdapter : RecyclerView.Adapter<FlightViewHolder>() {
    var mFlightsList: MutableList<Flight> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlightViewHolder {
        val callSignTextView = TextView(parent.context)
        return FlightViewHolder(callSignTextView)
    }

    override fun onBindViewHolder(holder: FlightViewHolder, position: Int) {
        Log.i(TAG, mFlightsList[position].callsign)
        holder.callSignView.text = mFlightsList[position].callsign
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
        var callSignView: TextView

        init {
            callSignView = itemView as TextView
        }
    }

    companion object {
        private val TAG = FlightListAdapter::class.java.simpleName
    }
}