package com.robybp.mytransferapp.screen.newguest.airplanebus

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.screen.dateandtimeofarrival.DateAndTimeViewModel
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import com.robybp.mytransferapp.screen.newguest.NewGuestViewModel
import com.robybp.mytransferapp.screen.pickapartment.PickApartmentViewModel
import com.robybp.mytransferapp.screen.pickdriver.PickDriverViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class NewGuestAirplaneBusFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var nameEditText: EditText
    private lateinit var flightNumberOrBusCompanyEditText: EditText
    private lateinit var arrivesFromEditText: EditText
    private lateinit var dateOfArrivalEditText: EditText
    private lateinit var arrivalTimeEditText: EditText
    private lateinit var driverEditText: EditText
    private lateinit var transferTypeHint: TextView
    private lateinit var apartmentNameEditText: EditText
    private lateinit var flightNumberOrBusCompanyText: TextView
    private lateinit var cancelButton: View
    private lateinit var saveButton: View
    private lateinit var sendInfoButton: View
    private lateinit var bottomIcon: ImageView
    private lateinit var topIcon: ImageView
    private val model: NewGuestViewModel by viewModel()
    private val sharedDateTimePickerViewModel: DateAndTimeViewModel by sharedViewModel()
    private val sharedPickDriverViewModel: PickDriverViewModel by sharedViewModel()
    private val sharedPickApartmentViewModel: PickApartmentViewModel by sharedViewModel()

    private val compositeDisposable = CompositeDisposable()

    private var listOfInputFields = listOf<EditText>()

    companion object {
        const val TAG = "NewGuestFragmentAirplaneBus"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_guest_airplane_bus, container, false)
        initialiseViews(view)
        sharedDateTimePickerViewModel.dateSetListener = this
        sharedDateTimePickerViewModel.timeSetListener = this
        sharedPickDriverViewModel.setName(null)
        sharedPickApartmentViewModel.setApartmentName(null)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (requireArguments()["Vehicle"] == MeansOfTransport.AIRPLANE.toString()) {
            topIcon.setImageResource(R.drawable.plane3)
            bottomIcon.setImageResource(R.drawable.plane3)
            flightNumberOrBusCompanyText.setText(R.string.newGuest_flightNumber_hint)
        } else {
            topIcon.setImageResource(R.drawable.bus)
            bottomIcon.setImageResource(R.drawable.bus)
            flightNumberOrBusCompanyText.setText(R.string.newGuest_busCompany_hint)
        }

        transferTypeHint.text = requireArguments().getString("TransferType")

        apartmentNameEditText.setText(requireArguments().getString("Apartment"))

        sharedPickDriverViewModel.getName().observe(viewLifecycleOwner,
            { driverEditText.setText(it) })

        sharedPickApartmentViewModel.getApartmentName().observe(viewLifecycleOwner,
            { if(it != null) apartmentNameEditText.setText(it) })

        dateOfArrivalEditText.setOnClickListener {
            model.showDatePicker()
        }

        arrivalTimeEditText.setOnClickListener {
            model.showTimePicker()
        }

        driverEditText.setOnClickListener {
            model.goToPickDriverFragment()
        }

        apartmentNameEditText.setOnClickListener {
            model.goToPickApartment()
        }

        saveButton.setOnClickListener {
            if (model.crucialFieldsEmpty(listOfInputFields)) {
                Toast.makeText(requireContext(), "No Fields can be empty", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            saveGuest()
        }

        cancelButton.setOnClickListener {
            sharedDateTimePickerViewModel.resetData()
            sharedPickApartmentViewModel.setApartmentName(null)
            compositeDisposable.dispose()
            model.goBack()
        }

        sendInfoButton.setOnClickListener {
            showAlertDialog()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun showAlertDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle(resources.getString(R.string.send_info_to_driver, driverEditText.text.toString()))
        dialog.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
            sendInfo()
        }
        dialog.setNegativeButton(resources.getString(R.string.cancel)) { _, _ ->
            return@setNegativeButton
        }
        dialog.show()
    }

    private fun saveGuest() {
        val guest = Guest(
            guestId = 0,
            name = nameEditText.text.toString(),
            vehicleInfo = flightNumberOrBusCompanyEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            transferType = transferTypeHint.text.toString(),
            driverName = driverEditText.text.toString(),
            apartmentName = apartmentNameEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            driverNotified = false,
            portOrStation = null
        )

        model.saveGuest(guest)
        sharedDateTimePickerViewModel.resetData()
        sharedPickApartmentViewModel.setApartmentName(null)
        model.returnToHOmeScreen()
    }

    private fun sendInfo() {

        if (model.crucialFieldsEmpty(listOfInputFields)) {
            Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG)
                .show()
            return
        }
        val guest = Guest(
            guestId = 0,
            name = nameEditText.text.toString(),
            vehicleInfo = flightNumberOrBusCompanyEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            driverName = driverEditText.text.toString(),
            transferType = transferTypeHint.text.toString(),
            apartmentName = apartmentNameEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            driverNotified = true,
            portOrStation = null
        )

        model.sendMessage(guest)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        sharedDateTimePickerViewModel.year = year
        sharedDateTimePickerViewModel.month = month
        sharedDateTimePickerViewModel.day = day
        val formatter = SimpleDateFormat("dd/MM/yyyy")
        val date = calendar.time
        val stringDate = formatter.format(date)
        dateOfArrivalEditText.setText(stringDate)
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onTimeSet(p0: TimePicker?, hours: Int, minutes: Int) {
        val formatter = SimpleDateFormat("HH:mm")
        val time = "$hours:$minutes"
        val date = formatter.parse(time)
        sharedDateTimePickerViewModel.hours = hours
        sharedDateTimePickerViewModel.minutes = minutes
        arrivalTimeEditText.setText(formatter.format(date))
    }

    private fun initialiseViews(view: View) {

        nameEditText = view.findViewById(R.id.newguestbusairplanescreen_guestName_editText)
        flightNumberOrBusCompanyEditText =
            view.findViewById(R.id.newguestbusairplanescreen_flightNumber_editText)
        arrivesFromEditText = view.findViewById(R.id.newguestbusairplanescreen_arrivesFrom_editText)
        dateOfArrivalEditText =
            view.findViewById(R.id.newguestbusairplanescreen_dateOfArrival_editText)
        arrivalTimeEditText =
            view.findViewById(R.id.newguestbusairplanescreen_timeOfArrival_editText)
        driverEditText = view.findViewById(R.id.newguestbusairplanescreen_driver_editText)
        transferTypeHint = view.findViewById(R.id.newguestbusairplanescreen_transferType_hint)
        apartmentNameEditText = view.findViewById(R.id.newguestbusairplanescreen_transferType_editText)
        apartmentNameEditText.inputType = EditorInfo.TYPE_NULL
        saveButton = view.findViewById(R.id.newguestbusairplanescreen_save_button)
        flightNumberOrBusCompanyText =
            view.findViewById(R.id.newguestbusairplanescreen_flightNumberOrBusCompany_hint)
        bottomIcon = view.findViewById(R.id.newguestbusairplanescreen_icon_bottom)
        topIcon = view.findViewById(R.id.newguestbusairplanescreen_top_icon)
        cancelButton = view.findViewById(R.id.newguestbusairplanescreen_cancel_button)
        sendInfoButton = view.findViewById(R.id.newguestbusairplanescreen_sendinfo_button)

        dateOfArrivalEditText.inputType = EditorInfo.TYPE_NULL
        arrivalTimeEditText.inputType = EditorInfo.TYPE_NULL
        driverEditText.inputType = EditorInfo.TYPE_NULL
        apartmentNameEditText.inputType = EditorInfo.TYPE_NULL

        listOfInputFields = listOf(
            flightNumberOrBusCompanyEditText,
            arrivesFromEditText,
            dateOfArrivalEditText,
            arrivalTimeEditText,
            driverEditText,
            flightNumberOrBusCompanyEditText,
            apartmentNameEditText
        )
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}
