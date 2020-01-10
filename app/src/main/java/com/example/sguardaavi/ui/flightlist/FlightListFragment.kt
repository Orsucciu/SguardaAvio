package com.example.sguardaavi.ui.flightlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sguardaavi.R

class FlightListFragment : Fragment() {
    private var mViewModel: FlightListViewModel? = null
    private var mAdapter: FlightListAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =
            inflater.inflate(R.layout.flight_list_fragment, container, false)
        val recyclerView: RecyclerView = root.findViewById(R.id.recyclerview)
        mAdapter = FlightListAdapter()
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(
            recyclerView.context,
            RecyclerView.VERTICAL,
            false
        )
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel =
            ViewModelProviders.of(this).get(FlightListViewModel::class.java)
        val arguments = arguments
        if (arguments != null) mViewModel!!.loadData(
            arguments.getLong(BEGIN),
            arguments.getLong(END),
            arguments.getBoolean(IS_ARRIVAL),
            arguments.getString(ICAO)
        )
        mViewModel!!.flightListLiveData.observe(
            viewLifecycleOwner,
            Observer { flights ->
                Log.i(
                    TAG,
                    "updating list with size = " + flights.size
                )
                mAdapter!!.setFlights(flights)
            })
    }

    companion object {
        private val TAG = FlightListFragment::class.java.simpleName
        private const val BEGIN = "begin"
        private const val END = "end"
        private const val IS_ARRIVAL = "isArrival"
        private const val ICAO = "icao"
        fun newInstance(): FlightListFragment {
            return FlightListFragment()
        }

        fun newInstance(
            begin: Long,
            end: Long,
            isArrival: Boolean,
            icao: String?
        ): FlightListFragment {
            val fragment = newInstance()
            val b = Bundle()
            b.putLong(BEGIN, begin)
            b.putLong(END, end)
            b.putBoolean(IS_ARRIVAL, isArrival)
            b.putString(ICAO, icao)
            fragment.arguments = b
            return fragment
        }
    }
}