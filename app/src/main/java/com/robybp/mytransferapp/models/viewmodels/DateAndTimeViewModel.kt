package com.robybp.mytransferapp.models.viewmodels

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.AndroidViewModel

class DateAndTimeViewModel(application: Application): AndroidViewModel(application) {

    var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null

    var timeSetListener: TimePickerDialog.OnTimeSetListener? = null
    var hours: Int? = null
    var minutes: Int? = null

    fun restData(){
        year = null
        month = null
        day = null
        hours = null
        minutes = null
    }
}
