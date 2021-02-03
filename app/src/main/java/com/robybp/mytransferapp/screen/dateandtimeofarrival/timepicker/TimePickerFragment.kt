package com.robybp.mytransferapp.screen.dateandtimeofarrival.timepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.robybp.mytransferapp.screen.dateandtimeofarrival.DateAndTimeViewModel

class TimePickerFragment(): DialogFragment() {

    private lateinit var model: DateAndTimeViewModel

    companion object{
        val TAG = "TimePickerFragment"
    }
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            DateAndTimeViewModel::class.java
        )

        val hours = model.hours ?: 12
        val minutes = model.minutes ?: 0

        return TimePickerDialog(
            requireContext(),
            TimePickerDialog.THEME_HOLO_LIGHT,
            model.timeSetListener,
            hours,
            minutes,
            true
        )
    }

}