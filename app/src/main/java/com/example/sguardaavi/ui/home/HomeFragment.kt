package com.example.sguardaavi.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sguardaavi.FlightApplication
import com.example.sguardaavi.R
import java.io.IOException
import com.example.sguardaavi.Utils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)

        homeViewModel.text.observe(this, Observer { myString ->
            textView.text = myString
        })

        val airports = Utils.generateAirportList()
        val airportsArray: MutableList<String> = ArrayList()
        airports.forEach{ airportsArray.add(it.name)}

        Log.i("Info", "MICHEL")
        Log.i("Info", airports.toString())


        val adapter = ArrayAdapter<String>(
            activity!!, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            airportsArray // Array
        )


        // Set the AutoCompleteTextView adapter
        root.autocompleteTextView.setAdapter(adapter)


        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        root.autocompleteTextView.threshold = 1


        // Set an item click listener for auto complete text view
        root.autocompleteTextView.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            Toast.makeText(FlightApplication.appContext,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }


        // Set a dismiss listener for auto complete text view
        root.autocompleteTextView.setOnDismissListener {
            Toast.makeText(FlightApplication.appContext,"Suggestion closed.",Toast.LENGTH_SHORT).show()
        }


        // Set a click listener for root layout
        root.root_layout.setOnClickListener{
            val text = root.autocompleteTextView.text
            Toast.makeText(FlightApplication.appContext,"Inputted : $text",Toast.LENGTH_SHORT).show()
        }


        // Set a focus change listener for auto complete text view
        root.autocompleteTextView.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                // Display the suggestion dropdown on focus
                root.autocompleteTextView.showDropDown()
            }
        }

        return root
    }
}