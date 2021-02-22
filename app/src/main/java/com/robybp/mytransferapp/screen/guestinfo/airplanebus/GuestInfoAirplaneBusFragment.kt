package com.robybp.mytransferapp.screen.guestinfo.airplanebus

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.robybp.mytransferapp.screen.guestinfo.GuestInfoViewModel
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import com.robybp.mytransferapp.screen.pickdriver.PickDriverViewModel
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class GuestInfoAirplaneBusFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        const val TAG = "GuestInfoAirplaneBusFragment"
    }

    private val compositeDisposable = CompositeDisposable()
    private val model: GuestInfoViewModel by viewModel()
    private val sharedPickDriverViewModel: PickDriverViewModel by sharedViewModel()
    private val sharedDateTimeViewModel: DateAndTimeViewModel by sharedViewModel()
    private lateinit var nameEditText: EditText
    private lateinit var flightNumberOrBusCompanyEditText: EditText
    private lateinit var arrivesFromEditText: EditText
    private lateinit var dateOfArrivalEditText: EditText
    private lateinit var arrivalTimeEditText: EditText
    private lateinit var driverEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var flightNumberOrBusCompanyText: TextView
    private lateinit var cancelButton: View
    private lateinit var saveChangesButton: View
    private lateinit var sendInfoButton: View
    private lateinit var bottomIcon: ImageView
    private lateinit var topIcon: ImageView
    private var guestId: Int? = null

    private var listOfInputFields = listOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new_guest_airplane_bus, container, false)
        sharedDateTimeViewModel.timeSetListener = this
        sharedDateTimeViewModel.dateSetListener = this
        sharedPickDriverViewModel.setName(null)
        initialiseViews(view)
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

        sharedPickDriverViewModel.getName().observe(viewLifecycleOwner,
            { driverEditText.setText(it) })

        model.queryGuest(requireArguments().getInt("GuestId"))
            .doOnSuccess{
                setInfo(it)
            }
            .subscribe()

        cancelButton.setOnClickListener {
            compositeDisposable.dispose()
            model.goBack()
        }

        dateOfArrivalEditText.setOnClickListener {
            model.showDatePicker()
        }

        arrivalTimeEditText.setOnClickListener {
            model.showTimePicker()
        }

        sendInfoButton.setOnClickListener {
            showAlertDialog()
        }

        driverEditText.setOnClickListener {
            model.goToPickDriverFragment()
        }

        saveChangesButton.setOnClickListener {
            if (model.crucialFieldsEmpty(listOfInputFields)) {
                Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            updateGuest()
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

    private fun sendInfo() {

        if (model.crucialFieldsEmpty(listOfInputFields)) {
            Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG)
                .show()
            return
        }
        val guest = Guest(
            guestId!!,
            name = nameEditText.text.toString(),
            vehicleInfo = flightNumberOrBusCompanyEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            driverName = driverEditText.text.toString(),
            apartmentName = noteEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            portOrStation = null
        )

        compositeDisposable.add(
            model.getDriverByName(guest.driverName!!)
                .doOnSuccess { Log.d("driver", it.name + it.phoneNumber) }
                .subscribe { formatMessage(it.phoneNumber, guest) }
        )
    }

    private fun formatMessage(phoneNumber: String, guest: Guest) {

        val flightNumberOrBusCompany =
            if (guest.meansOfTransport == MeansOfTransport.BUS.toString()) getString(R.string.messageInfo_busCompany_hint) else getString(
                R.string.messageInfo_flightNumber_hint
            )
        val messageBody = getString(
            R.string.airplaneOrBus_messageBody,
            getString(R.string.messageInfo_guestName_hint),
            guest.name,
            flightNumberOrBusCompany,
            guest.vehicleInfo,
            getString(R.string.messageInfo_arrival_hint),
            guest.countryOfArrival,
            getString(R.string.messageInfo_dateAndTimeOfArrival),
            guest.dateOfArrival,
            guest.timeOfArrival,
            guest.apartmentName
        )

        phoneNumber.let {
            model.sendMessage(messageBody, it)
            compositeDisposable.dispose()
            model.updateGuest(guest)
            model.goBack()
        }
    }

    private fun updateGuest(){
        val guest = Guest(
            guestId!!,
            name = nameEditText.text.toString(),
            vehicleInfo = flightNumberOrBusCompanyEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            driverName = driverEditText.text.toString(),
            apartmentName = noteEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            portOrStation = null
        )

        model.updateGuest(guest)
        sharedDateTimeViewModel.resetData()
        model.goBack()
    }

    @SuppressLint("SimpleDateFormat")
    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)

        sharedDateTimeViewModel.year = year
        sharedDateTimeViewModel.month = month
        sharedDateTimeViewModel.day = day
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
        sharedDateTimeViewModel.hours = hours
        sharedDateTimeViewModel.minutes = minutes
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
        noteEditText = view.findViewById(R.id.newguestbusairplanescreen_phone_editText)
        saveChangesButton = view.findViewById(R.id.newguestbusairplanescreen_save_button)
        flightNumberOrBusCompanyText =
            view.findViewById(R.id.newguestbusairplanescreen_flightNumberOrBusCompany_hint)
        bottomIcon = view.findViewById(R.id.newguestbusairplanescreen_icon_bottom)
        topIcon = view.findViewById(R.id.newguestbusairplanescreen_top_icon)
        cancelButton = view.findViewById(R.id.newguestbusairplanescreen_cancel_button)
        sendInfoButton = view.findViewById(R.id.newguestbusairplanescreen_sendinfo_button)

        dateOfArrivalEditText.inputType = EditorInfo.TYPE_NULL
        arrivalTimeEditText.inputType = EditorInfo.TYPE_NULL
        driverEditText.inputType = EditorInfo.TYPE_NULL

        listOfInputFields = listOf(
            flightNumberOrBusCompanyEditText,
            arrivesFromEditText,
            dateOfArrivalEditText,
            arrivalTimeEditText,
            driverEditText,
            flightNumberOrBusCompanyEditText
        )
    }

    private fun setInfo(guest: Guest){
        guestId = guest.guestId
        nameEditText.setText(guest.name)
        flightNumberOrBusCompanyEditText.setText(guest.vehicleInfo)
        arrivesFromEditText.setText(guest.countryOfArrival)
        dateOfArrivalEditText.setText(guest.dateOfArrival)
        arrivalTimeEditText.setText(guest.timeOfArrival)
        driverEditText.setText(guest.driverName)
        noteEditText.setText(guest.apartmentName)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}
