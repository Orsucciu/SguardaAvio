package com.example.sguardaavi.ui.home

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sguardaavi.*
import com.example.sguardaavi.data.Airport
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.Console
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*
import kotlin.collections.ArrayList

//https://android--code.blogspot.com/2018/02/android-kotlin-autocompletetextview.html tuto for the autocomplete thing

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var fromDate: EditText
    lateinit var toDate: EditText


    private lateinit var airports : List<Airport>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val switchD: Switch = root.findViewById(R.id.switch1)

        fromDate = root.findViewById(R.id.dateD)
        toDate = root.findViewById(R.id.dateA)

        homeViewModel.airport.observe(this, Observer { myString ->
            textView.text = myString
        })
        homeViewModel.isDeparture.observe(this, Observer { isChecked ->
            switchD.isChecked = isChecked
        })
        homeViewModel.fromDate.observe(this, Observer { epochDay ->
            updateDateLabel(fromDate, epochDay)
        })
        homeViewModel.toDate.observe(this, Observer { epochDay ->
            updateDateLabel(toDate, epochDay)
        })


        airports = Utils.generateAirportList()

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
        root.text_home.setAdapter(adapter)


        // Auto complete threshold
        // The minimum number of characters to type to show the drop down
        root.text_home.threshold = 1


        // Set an item click listener for auto complete text view
        root.text_home.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            Toast.makeText(FlightApplication.appContext,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }


        // Set a dismiss listener for auto complete text view
        root.text_home.setOnDismissListener {
            Toast.makeText(FlightApplication.appContext,"Suggestion closed.",Toast.LENGTH_SHORT).show()
        }


        // Set a focus change listener for auto complete text view
        root.text_home.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if (b) {
                // Display the suggestion dropdown on focus
                root.text_home.showDropDown()
            }
        }

        switchD.setOnCheckedChangeListener { buttonView, isChecked ->

            homeViewModel.isDeparture.value = isChecked


            //this logic changed the switch to departure/arrival. Used for screen 2 (this is screen one)
            if(switchD.isChecked){
                switchD.text = "Arrivee"
                //root.textViewD.isEnabled = false
                //root.dateD.isEnabled = false

                //root.textViewA.isEnabled = true
                //root.dateA.isEnabled = true

            }else {
                switchD.text = "Depart"
                //root.textViewD.isEnabled = true
                //root.dateD.isEnabled = true

                //root.textViewA.isEnabled = false
                //root.dateA.isEnabled = false
            }
        }



        fromDate.setOnClickListener(View.OnClickListener { view ->
            val currentCalendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                view.context,
                fromDateListener,
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH)
            )
            val datePicker = datePickerDialog.datePicker
            currentCalendar.add(Calendar.DAY_OF_MONTH, -1)
            //datePicker.maxDate = currentCalendar.timeInMillis
            datePickerDialog.show()
        })

        toDate.setOnClickListener(View.OnClickListener { view ->
            val currentCalendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                view.context,
                toDateListener,
                currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH),
                currentCalendar.get(Calendar.DAY_OF_MONTH)
            )
            val datePicker = datePickerDialog.datePicker
            currentCalendar.add(Calendar.DAY_OF_MONTH, 1)
            //datePicker.maxDate = currentCalendar.timeInMillis
            datePickerDialog.show()
        })

        root.buttonGO.setOnClickListener(View.OnClickListener {
            if (text_home.text.toString() != "") {
                val timeD: String = toDate.text.toString()
                val timeA: String = fromDate.text.toString()
                val situation: String

                if (!switchD.isChecked) {
                    situation = "Departure from "
                } else {
                    situation = "Arrival at "
                }

                Toast.makeText(
                    getActivity(),
                    situation + " " + text_home.text.toString() + ", " + timeA + " " + timeD,
                    Toast.LENGTH_SHORT
                ).show()

                val begin = LocalDate.ofEpochDay(homeViewModel.fromDate.value!!).atStartOfDay().toEpochSecond(ZoneOffset.UTC)
                val end = LocalDate.ofEpochDay(homeViewModel.toDate.value!!).atStartOfDay().toEpochSecond(ZoneOffset.UTC)
                val isArrival = !homeViewModel.isDeparture.value!!
                val icao = getIcao(textView.text.toString())

                Log.i("Info", "$icao ICAO SELECTED")

                if (icao != null) {
                    Log.i("Info","Launching flights list....")
                    GlobalActivity.startActivity(this.requireActivity(), begin, end, isArrival, icao)

                } else {
                    Toast.makeText(
                        getActivity(),
                        "Unable to find airport name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        return root
    }

    fun getIcao(airportName : String) : String? {

        val airportNameLowerCase = airportName.toLowerCase()

        for (airportObject in airports) {
            if (airportObject.name.toLowerCase().equals(airportNameLowerCase)) {
                return airportObject.icao
            }
        }

        return null
    }

    fun updateDateLabel(dateEditText: EditText, epochDay: Long) {
        val date = LocalDate.ofEpochDay(epochDay)
        dateEditText.setText(
            date.dayOfMonth.toString() + "/" + date.monthValue.toString() + "/" + date.year.toString()
        )
    }

    val fromDateListener =
        DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            homeViewModel.fromDate.value = LocalDate.of(year, month + 1, dayOfMonth).toEpochDay()
        }

    val toDateListener =
        DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            homeViewModel.toDate.value = LocalDate.of(year, month + 1, dayOfMonth).toEpochDay()
        }
}




