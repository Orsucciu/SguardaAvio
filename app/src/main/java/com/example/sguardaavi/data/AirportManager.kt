package com.example.sguardaavi.data

import com.example.sguardaavi.Utils.Companion.generateAirportList

class AirportManager private constructor() {
    val airportList: List<Airport>
    fun getAirportByIndex(index: Int): Airport {
        return airportList[index]
    }

    companion object {
        var instance: AirportManager? = null
            get() {
                if (field == null) {
                    field = AirportManager()
                }
                return field
            }
            private set
    }

    init {
        airportList = generateAirportList()
    }
}