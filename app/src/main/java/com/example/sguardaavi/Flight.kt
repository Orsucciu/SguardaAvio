package com.example.sguardaavi

/**
 * Created by theo
 */
data class Flight (
    var estDepartureAirport : String,
    var estArrivalAirport : String,
    var firstSeen : Long,
    var lastSeen : Long,
    var callsign : String

) {
    fun getDurationTime() : Long {
        return lastSeen - firstSeen
    }
}