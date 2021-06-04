package com.robybp.mytransferapp.screen.dateandtimeofarrival.datepicker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.robybp.mytransferapp.screen.dateandtimeofarrival.DateAndTimeViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.*

class DatePickerFragment : DialogFragment() {

    companion object {
        const val TAG = "DatePickerFragment"
    }

    private val model: DateAndTimeViewModel by sharedViewModel()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val instance = Calendar.getInstance()

        val year = model.year ?: instance.get(Calendar.YEAR)
        val month = model.month ?: instance.get(Calendar.MONTH)
        val monthDay = model.day ?: instance.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), model.dateSetListener, year, month, monthDay)
        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000

        return datePicker
    }
}
