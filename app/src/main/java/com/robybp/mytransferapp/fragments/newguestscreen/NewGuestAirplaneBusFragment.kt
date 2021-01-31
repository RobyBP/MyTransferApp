package com.robybp.mytransferapp.fragments.newguestscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.robybp.mytransferapp.MeansOfTransport
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.fragments.datepickerscreen.DatePickerFragment
import com.robybp.mytransferapp.fragments.timepickerscreen.TimePickerFragment
import com.robybp.mytransferapp.models.datamodels.Guest
import com.robybp.mytransferapp.models.viewmodels.DateAndTimeViewModel
import com.robybp.mytransferapp.models.viewmodels.NewGuestAirplaneBusViewModel
import org.joda.time.Days
import org.joda.time.LocalDate
import java.text.DateFormat
import java.util.*

class NewGuestAirplaneBusFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var nameEditText: EditText
    private lateinit var flightNumberOrBusCompanyEditText: EditText
    private lateinit var arrivesFromEditText: EditText
    private lateinit var dateOfArrivalEditText: EditText
    private lateinit var arrivalTimeEditText: EditText
    private lateinit var driverEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var flightNumberOrBusCompanyText: TextView
    private lateinit var cancelButton: View
    private lateinit var saveButton: View
    private lateinit var sendInfoButton: View
    private lateinit var bottomIcon: ImageView
    private lateinit var topIcon: ImageView
    private lateinit var model: NewGuestAirplaneBusViewModel
    private lateinit var sharedDateTimePickerViewModel: DateAndTimeViewModel

    private var listOfInputFields = listOf<EditText>()

    companion object {
        val TAG = "NewGuestFragmentAirplaneBus"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_guest_airplane_bus, container, false)
        initialiseViews(view)
        initialiseViewModels()
        sharedDateTimePickerViewModel.dateSetListener = this
        sharedDateTimePickerViewModel.timeSetListener = this
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (arguments!!["Vehicle"] == MeansOfTransport.AIRPLANE.toString()) {
            topIcon.setImageResource(R.drawable.plane3)
            bottomIcon.setImageResource(R.drawable.plane3)
            flightNumberOrBusCompanyText.setText(R.string.newGuest_flightNumber_hint)
        } else {
            topIcon.setImageResource(R.drawable.bus)
            bottomIcon.setImageResource(R.drawable.bus)
            flightNumberOrBusCompanyText.setText(R.string.newGuest_busCompany_hint)
        }

        dateOfArrivalEditText.setOnClickListener {
            val datePicker = DatePickerFragment()
            datePicker.show(fragmentManager!!, DatePickerFragment.TAG)
        }

        arrivalTimeEditText.setOnClickListener {
            val timePicker = TimePickerFragment()
            timePicker.show(fragmentManager!!, TimePickerFragment.TAG)
        }

        saveButton.setOnClickListener {
            if(model.crucialFieldsEmpty(listOfInputFields)){
                Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            saveMember()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveMember(){
        val guest = Guest(
            guestId = 0,
            name = nameEditText.text.toString(),
            vehicleInfo = flightNumberOrBusCompanyEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            driverName = driverEditText.text.toString(),
            note = noteEditText.text.toString(),
            daysUntilArrival = Days.daysBetween(
                LocalDate.now(),
                LocalDate(
                    sharedDateTimePickerViewModel.year!!,
                    sharedDateTimePickerViewModel.month!! + 1,
                    sharedDateTimePickerViewModel.day!!
                )
            ).days,
            meansOfTransport = arguments!!["Vehicle"].toString(),
            portOrStation = null
        )

        model.saveGuest(guest)
        fragmentManager!!.popBackStack()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        sharedDateTimePickerViewModel.year = year
        sharedDateTimePickerViewModel.month = month
        sharedDateTimePickerViewModel.day = day
        val date = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.time)
        dateOfArrivalEditText.setText(date)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {
        val formater = SimpleDateFormat("HH:mm")
        val time = "$hours:$minutes"
        val date = formater.parse(time)
        sharedDateTimePickerViewModel.hours = hours
        sharedDateTimePickerViewModel.minutes = minutes
        arrivalTimeEditText.setText(formater.format(date))
    }

    private fun initialiseViews(view: View) {

        nameEditText = view.findViewById(R.id.driversmenuscreen_driverName_editText)
        flightNumberOrBusCompanyEditText =
            view.findViewById(R.id.newguestbusairplanescreen_flightNumber_editText)
        arrivesFromEditText = view.findViewById(R.id.newguestbusairplanescreen_arrivesFrom_editText)
        dateOfArrivalEditText =
            view.findViewById(R.id.newguestbusairplanescreen_dateOfArrival_editText)
        arrivalTimeEditText =
            view.findViewById(R.id.newguestbusairplanescreen_timeOfArrival_editText)
        driverEditText = view.findViewById(R.id.newguestbusairplanescreen_driver_editText)
        noteEditText = view.findViewById(R.id.newguestbusairplanescreen_note_editText)
        saveButton = view.findViewById(R.id.newguestbusairplanescreen_save_button)
        flightNumberOrBusCompanyText =
            view.findViewById(R.id.newguestbusairplanescreen_flightNumberOrBusCompany_hint)
        bottomIcon = view.findViewById(R.id.newguestbusairplanescreen_icon_bottom)
        topIcon = view.findViewById(R.id.newguestbusairplanescreen_top_icon)
        cancelButton = view.findViewById(R.id.newguestbusairplanescreen_cancel_button)
        sendInfoButton = view.findViewById(R.id.newguestbusairplanescreen_sendinfo_button)

        dateOfArrivalEditText.inputType = EditorInfo.TYPE_NULL
        arrivalTimeEditText.inputType = EditorInfo.TYPE_NULL

        listOfInputFields = listOf(
            flightNumberOrBusCompanyEditText,
            arrivesFromEditText,
            dateOfArrivalEditText,
            arrivalTimeEditText,
            driverEditText,
            flightNumberOrBusCompanyEditText
        )
    }

    private fun initialiseViewModels() {

        model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            NewGuestAirplaneBusViewModel::class.java
        )

        sharedDateTimePickerViewModel = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            DateAndTimeViewModel::class.java
        )
    }

    override fun onDestroyView() {
        sharedDateTimePickerViewModel.restData()
        super.onDestroyView()
    }
}
