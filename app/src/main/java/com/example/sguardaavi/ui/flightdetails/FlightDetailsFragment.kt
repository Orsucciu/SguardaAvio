package com.example.sguardaavi.ui.flightdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sguardaavi.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment


class FlightDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var gMap: GoogleMap

    private lateinit var mapFragment: SupportMapFragment
    private var mViewModel: FlightDetailsViewModel? = null
    private var mAdapter: FlightDetailsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View =
            inflater.inflate(R.layout.flightdetails_fragment, container, false)
        val arguments = arguments
        mapFragment = (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?)!!
        mViewModel =
            ViewModelProviders.of(this).get<FlightDetailsViewModel>(FlightDetailsViewModel::class.java)

        if (mapFragment == null) {
            val fragmentManager = fragmentManager
            val fragmentTransaction =
                fragmentManager!!.beginTransaction()
            mapFragment = SupportMapFragment.newInstance()
            fragmentTransaction.replace(R.id.map, mapFragment).commit()
        }

        mapFragment.getMapAsync(this)
        return root
    }

    companion object {
        private val TAG = FlightDetailsFragment::class.java.simpleName
        private const val FIRSTSEEN = "firstseen"
        private const val ICAO = "icao"
        fun newInstance(): FlightDetailsFragment {
            return FlightDetailsFragment()
        }

        fun newInstance(
            firstseen: Long,
            icao: String?
        ): FlightDetailsFragment {
            val fragment = newInstance()
            val b = Bundle()
            b.putLong(FIRSTSEEN, firstseen)
            b.putString(ICAO, icao)
            fragment.arguments = b
            return fragment
        }
    }

    override fun onMapReady(googlemap: GoogleMap?) {
        if (googlemap != null) {
            gMap = googlemap
        }
    }
}