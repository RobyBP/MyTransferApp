package com.robybp.mytransferapp.fragments.datepickerscreen

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.robybp.mytransferapp.models.viewmodels.DateAndTimeViewModel
import java.util.*

class DatePickerFragment() : DialogFragment() {

    private lateinit var model: DateAndTimeViewModel

    companion object {
        val TAG = "DatePickerFragment"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            DateAndTimeViewModel::class.java
        )

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

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

        return datePicker
    }
}
