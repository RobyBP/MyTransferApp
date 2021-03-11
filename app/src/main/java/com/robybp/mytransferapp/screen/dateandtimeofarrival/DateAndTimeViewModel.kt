package com.robybp.mytransferapp.screen.dateandtimeofarrival

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.lifecycle.ViewModel

class DateAndTimeViewModel : ViewModel() {

    var dateSetListener: DatePickerDialog.OnDateSetListener? = null
    var year: Int? = null
    var month: Int? = null
    var day: Int? = null

    var timeSetListener: TimePickerDialog.OnTimeSetListener? = null
    var hours: Int? = null
    var minutes: Int? = null

    fun resetData() {
        year = null
        month = null
        day = null
        hours = null
        minutes = null
    }
}
