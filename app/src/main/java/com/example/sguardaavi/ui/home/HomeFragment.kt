package com.example.sguardaavi.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.sguardaavi.FlightApplication
import com.example.sguardaavi.R
import com.example.sguardaavi.Utils
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.*
import kotlin.collections.ArrayList

//https://android--code.blogspot.com/2018/02/android-kotlin-autocompletetextview.html tuto for the autocomplete thing

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    lateinit var fromDate: EditText
    lateinit var toDate: EditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        val switchD: Switch = root.findViewById(R.id.switch1)

        fromDate = root.findViewById(R.id.dateD)
        toDate = root.findViewById(R.id.dateA)

        val currentCalendar = Calendar.getInstance()
        updateDateLabel(fromDate, currentCalendar)
        updateDateLabel(toDate, currentCalendar)

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
            if(b){
                // Display the suggestion dropdown on focus
                root.text_home.showDropDown()
            }
        }

        /*switchD.setOnCheckedChangeListener { buttonView, isChecked ->

            if(switchD.isChecked){
                switchD.text = "Arrivee"
                root.textViewD.isEnabled = false
                root.dateD.isEnabled = false

                root.textViewA.isEnabled = true
                root.dateA.isEnabled = true

            }else {
                switchD.text = "Depart"
                root.textViewD.isEnabled = true
                root.dateD.isEnabled = true

                root.textViewA.isEnabled = false
                root.dateA.isEnabled = false
            }
        }*/

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
            datePicker.maxDate = currentCalendar.timeInMillis
            datePickerDialog.show()
        })

        toDate.setOnClickListener(View.OnClickListener { view ->
            val currentCalendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                view.context, toDateListener, currentCalendar.get(Calendar.YEAR),
                currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH)
            )
            val datePicker = datePickerDialog.datePicker
            currentCalendar.add(Calendar.DAY_OF_MONTH, -1)
            datePicker.maxDate = currentCalendar.timeInMillis
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
                    FlightApplication.appContext,
                    situation + " " + text_home.text.toString() + ", " + timeA + " " + timeD,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        return root
    }

    fun updateDateLabel(dateEditText: EditText, calendar: Calendar) {
        dateEditText.setText(
            calendar.get(Calendar.DAY_OF_MONTH).toString() + "/" + calendar.get(
                Calendar.MONTH
            ) + "/" + calendar.get(Calendar.YEAR)
        )
    }

    val fromDateListener =
        DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel(fromDate, calendar)
        }

    val toDateListener =
        DatePickerDialog.OnDateSetListener { datePicker, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateLabel(toDate, calendar)
        }
}




