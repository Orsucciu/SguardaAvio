package com.example.sguardaavi.data;

import com.example.sguardaavi.Utils;
import java.util.List;

public class AirportManager
{
    private static AirportManager instance;

    private List<Airport> airportList;

    private AirportManager(){
        airportList = Utils.Companion.generateAirportList();
    }

    public static AirportManager getInstance(){
        if(instance == null){
            instance = new AirportManager();
        }
        return instance;
    }

    public Airport getAirportByIndex(int index){
        return airportList.get(index);
    }

    public List<Airport> getAirportList()
    {
        return airportList;
    }
}
