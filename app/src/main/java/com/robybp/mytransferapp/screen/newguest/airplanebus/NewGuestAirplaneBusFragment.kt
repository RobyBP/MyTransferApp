package com.robybp.mytransferapp.screen.newguest.airplanebus

import android.annotation.SuppressLint
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.screen.dateandtimeofarrival.DateAndTimeViewModel
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import com.robybp.mytransferapp.screen.newguest.NewGuestViewModel
import com.robybp.mytransferapp.screen.pickdriver.PickDriverViewModel
import com.robybp.mytransferapp.util.NotificationWorker
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.concurrent.TimeUnit

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
    private val model: NewGuestViewModel by viewModel()
    private val sharedDateTimePickerViewModel: DateAndTimeViewModel by sharedViewModel()
    private val sharedPickDriverViewModel: PickDriverViewModel by sharedViewModel()

    private val compositeDisposable = CompositeDisposable()

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
        sharedDateTimePickerViewModel.dateSetListener = this
        sharedDateTimePickerViewModel.timeSetListener = this
        sharedPickDriverViewModel.setName(null)
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

        dateOfArrivalEditText.setOnClickListener {
            model.showDatePicker()
        }

        arrivalTimeEditText.setOnClickListener {
            model.showTimePicker()
        }

        driverEditText.setOnClickListener {
            model.goToPickDriverFragment()
        }

        saveButton.setOnClickListener {
            if (model.crucialFieldsEmpty(listOfInputFields)) {
                Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }
            saveGuest()
        }

        cancelButton.setOnClickListener {
            sharedDateTimePickerViewModel.resetData()
            compositeDisposable.dispose()
            model.goBack()
        }

        sendInfoButton.setOnClickListener {
            sendInfo()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun saveGuest() {
        val guest = Guest(
            guestId = 0,
            name = nameEditText.text.toString(),
            vehicleInfo = flightNumberOrBusCompanyEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            driverName = driverEditText.text.toString(),
            note = noteEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            portOrStation = null
        )

        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(guest.dateOfArrival, formatter)
        val diff = ChronoUnit.DAYS.between(LocalDate.now().plusDays(2), date) * 12
        val uploadWorkRequest: WorkRequest? =
            if (ChronoUnit.DAYS.between(
                    LocalDate.now(),
                    date
                ) in 2..3
            ) OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                12,
                TimeUnit.HOURS
            ).build()
            else OneTimeWorkRequestBuilder<NotificationWorker>().setInitialDelay(
                diff,
                TimeUnit.HOURS
            ).build()

        if (uploadWorkRequest != null) {
            WorkManager
                .getInstance(requireContext())
                .enqueue(uploadWorkRequest)
        }


        model.saveGuest(guest)
        sharedDateTimePickerViewModel.resetData()
        model.goBack()
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
            note = noteEditText.text.toString(),
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

        val flightNuderOrBusCompany =
            if (guest.meansOfTransport == MeansOfTransport.BUS.toString()) getString(R.string.messageInfo_busCompany_hint) else getString(
                R.string.messageInfo_flightNumber_hint
            )
        val messageBody = getString(
            R.string.airplaneOrBus_messageBody,
            getString(R.string.messageInfo_guestName_hint),
            guest.name,
            flightNuderOrBusCompany,
            guest.vehicleInfo,
            getString(R.string.messageInfo_arrival_hint),
            guest.countryOfArrival,
            getString(R.string.messageInfo_dateAndTimeOfArrival),
            guest.dateOfArrival,
            guest.timeOfArrival,
            guest.note
        )

        phoneNumber.let {
            model.sendMessage(messageBody, it)
            compositeDisposable.dispose()
            model.saveGuest(guest)
            model.goBack()
        }
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

        nameEditText = view.findViewById(R.id.newguestbusairplanescreen_guestName_editText)
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

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}
