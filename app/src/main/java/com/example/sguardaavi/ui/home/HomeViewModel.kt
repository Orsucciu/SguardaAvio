package com.example.sguardaavi.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class HomeViewModel : ViewModel() {

    private val _airport = MutableLiveData<String>().apply {
        value = ""
    }
    private val _isDeparture = MutableLiveData<Boolean>().apply {
        value = true
    }
    private val _fromDate = MutableLiveData<Long>().apply {
        value = LocalDate.now().toEpochDay()
    }
    private val _toDate = MutableLiveData<Long>().apply {
        value = LocalDate.now().toEpochDay()
    }

    val airport: MutableLiveData<String> = _airport
    val isDeparture: MutableLiveData<Boolean> = _isDeparture
    val fromDate : MutableLiveData<Long> = _fromDate
    val toDate : MutableLiveData<Long> = _toDate


}