package com.robybp.mytransferapp.screen.dateandtimeofarrival.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.robybp.mytransferapp.screen.dateandtimeofarrival.DateAndTimeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class DatePickerFragment() : DialogFragment() {

    private val model: DateAndTimeViewModel by sharedViewModel()

    companion object {
        val TAG = "DatePickerFragment"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val year = model.year ?: Calendar.getInstance().get(Calendar.YEAR)
        val month = model.month ?: Calendar.getInstance().get(Calendar.MONTH)
        val monthDay = model.day ?: Calendar.getInstance().get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(),
            model.dateSetListener,
            year,
            month,
            monthDay
        )

        //datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

        return datePicker
    }
}
