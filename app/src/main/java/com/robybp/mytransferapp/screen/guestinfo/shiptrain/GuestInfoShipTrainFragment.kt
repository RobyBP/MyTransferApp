package com.robybp.mytransferapp.screen.guestinfo.shiptrain

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

class GuestInfoShipTrainFragment : Fragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    companion object {
        const val TAG = "GuestInfoShipTrainFragment"
    }

    private val compositeDisposable = CompositeDisposable()
    private val model: GuestInfoViewModel by viewModel()
    private val sharedPickDriverViewModel: PickDriverViewModel by sharedViewModel()
    private val sharedDateTimeViewModel: DateAndTimeViewModel by sharedViewModel()
    private lateinit var topIcon: ImageView
    private lateinit var bottomIcon: ImageView
    private lateinit var trainOrShipNumberHint: TextView
    private lateinit var portOrStationHint: TextView
    private lateinit var saveChangesButton: View
    private lateinit var cancelButton: View
    private lateinit var guestNameEditText: EditText
    private lateinit var shipOrTrainNumberEditText: EditText
    private lateinit var portOrStationEditText: EditText
    private lateinit var arrivesFromEditText: EditText
    private lateinit var dateOfArrivalEditText: EditText
    private lateinit var arrivalTimeEditText: EditText
    private lateinit var driverEditText: EditText
    private lateinit var noteEditText: EditText
    private lateinit var sendInfoButton: View
    private var guestId: Int? = null

    private var inputFields = listOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new_guest_ship_train, container, false)
        sharedDateTimeViewModel.timeSetListener = this
        sharedDateTimeViewModel.dateSetListener = this
        sharedPickDriverViewModel.setName(null)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if (requireArguments()["Vehicle"] == MeansOfTransport.SHIP.toString()) {
            topIcon.setImageResource(R.drawable.ship)
            bottomIcon.setImageResource(R.drawable.ship)
            trainOrShipNumberHint.text = getString(R.string.newGuest_shipLineNumber_hint)
            portOrStationHint.text = getString(R.string.newGuest_shipOnPort_hint)
        } else {
            topIcon.setImageResource(R.drawable.train)
            bottomIcon.setImageResource(R.drawable.train)
            trainOrShipNumberHint.text = getString(R.string.newGuest_trainNumber_hint)
            portOrStationHint.text = getString(R.string.newGuest_trainOnStation_hint)
        }

        model.queryGuest(requireArguments().getInt("GuestId"))
            .doOnSuccess {
                setInfo(it)
            }
            .subscribe()

        sharedPickDriverViewModel.getName().observe(viewLifecycleOwner,
            { driverEditText.setText(it) })


        dateOfArrivalEditText.setOnClickListener {
            model.showDatePicker()
        }

        arrivalTimeEditText.setOnClickListener {
            model.showTimePicker()
        }

        cancelButton.setOnClickListener {
            compositeDisposable.dispose()
            model.goBack()
        }

        sendInfoButton.setOnClickListener {
            showAlertDialog()
        }

        driverEditText.setOnClickListener {
            model.goToPickDriverFragment()
        }
        saveChangesButton.setOnClickListener {
            if (model.crucialFieldsEmpty(inputFields)) {
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

        if (model.crucialFieldsEmpty(inputFields)) {
            Toast.makeText(requireContext(), "Only note field can be empty", Toast.LENGTH_LONG)
                .show()
            return
        }
        val guest = Guest(
            guestId = 0,
            name = guestNameEditText.text.toString(),
            vehicleInfo = shipOrTrainNumberEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            driverName = driverEditText.text.toString(),
            transferType = noteEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            portOrStation = portOrStationEditText.text.toString()
        )

        compositeDisposable.add(
            model.getDriverByName(guest.driverName!!)
                .doOnSuccess { Log.d("driver", it.name + it.phoneNumber) }
                .subscribe { formatMessage(it.phoneNumber, guest) }
        )
    }

    private fun formatMessage(phoneNumber: String, guest: Guest) {
        val shipOrTrainNumber =
            if (guest.meansOfTransport == MeansOfTransport.SHIP.toString()) getString(R.string.messageInfo_shipNumber_hint) else getString(
                R.string.messageInfo_trainNumber_hint
            )

        val portOrStation =
            if (guest.meansOfTransport == MeansOfTransport.SHIP.toString()) getString(R.string.newGuest_shipOnPort_hint) else getString(
                R.string.newGuest_trainOnStation_hint
            )

        val messageBody = getString(
            R.string.shipOrTrain_messageBody,
            getString(R.string.messageInfo_guestName_hint),
            guest.name,
            shipOrTrainNumber,
            guest.vehicleInfo,
            portOrStation,
            guest.portOrStation,
            getString(R.string.messageInfo_arrival_hint),
            guest.countryOfArrival,
            getString(R.string.messageInfo_dateAndTimeOfArrival),
            guest.dateOfArrival,
            guest.timeOfArrival,
            guest.transferType
        )

        phoneNumber.let {
            model.sendMessage(messageBody, it)
            compositeDisposable.dispose()
            model.updateGuest(guest)
            model.goBack()
        }
    }

    private fun updateGuest() {
        val guest = Guest(
            guestId!!,
            name = guestNameEditText.text.toString(),
            vehicleInfo = shipOrTrainNumberEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            driverName = driverEditText.text.toString(),
            transferType = noteEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            portOrStation = portOrStationEditText.text.toString()
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
        val formater = SimpleDateFormat("HH:mm")
        val time = "$hours:$minutes"
        val date = formater.parse(time)
        sharedDateTimeViewModel.hours = hours
        sharedDateTimeViewModel.minutes = minutes
        arrivalTimeEditText.setText(formater.format(date))
    }

    private fun initialiseViews(view: View) {
        topIcon = view.findViewById(R.id.newguestshiptrainscreen_top_icon)
        bottomIcon = view.findViewById(R.id.newguestshiptrainscreen_icon_bottom)
        trainOrShipNumberHint =
            view.findViewById(R.id.newguestshiptrainscreen_trainOrShipNumber_hint)
        portOrStationHint = view.findViewById(R.id.newguestshiptrainscreen_portOrStation_hint)
        saveChangesButton = view.findViewById(R.id.newguestshiptrainscreen_save_button)
        cancelButton = view.findViewById(R.id.newguestshiptrainscreen_cancel_button)
        guestNameEditText = view.findViewById(R.id.newguestshiptrainscreen_driverName_editText)
        shipOrTrainNumberEditText =
            view.findViewById(R.id.newguestshiptrainscreen_trainOrShipNumber_editText)
        portOrStationEditText =
            view.findViewById(R.id.newguestshiptrainscreen_portOrStation_editText)
        arrivesFromEditText = view.findViewById(R.id.newguestshiptrainscreen_arrivesFrom_editText)
        dateOfArrivalEditText =
            view.findViewById(R.id.newguestshiptrainscreen_dateOfArrival_editText)
        arrivalTimeEditText = view.findViewById(R.id.newguestshiptrainscreen_timeOfArrival_editText)
        driverEditText = view.findViewById(R.id.newguestshiptrainscreen_driver_editText)
        noteEditText = view.findViewById(R.id.newguestshiptrainscreen_transferType_editText)
        sendInfoButton = view.findViewById(R.id.newguestshiptrainscreen_sendinfo_button)

        driverEditText.inputType = EditorInfo.TYPE_NULL
        dateOfArrivalEditText.inputType = EditorInfo.TYPE_NULL
        arrivalTimeEditText.inputType = EditorInfo.TYPE_NULL

        inputFields = listOf(
            guestNameEditText,
            shipOrTrainNumberEditText,
            portOrStationEditText,
            arrivesFromEditText,
            arrivalTimeEditText,
            dateOfArrivalEditText,
            driverEditText
        )
    }

    private fun setInfo(guest: Guest) {
        guestId = guest.guestId
        guestNameEditText.setText(guest.name)
        arrivesFromEditText.setText(guest.countryOfArrival)
        shipOrTrainNumberEditText.setText(guest.vehicleInfo)
        portOrStationEditText.setText(guest.portOrStation)
        dateOfArrivalEditText.setText(guest.dateOfArrival)
        arrivalTimeEditText.setText(guest.timeOfArrival)
        driverEditText.setText(guest.driverName)
        noteEditText.setText(guest.transferType)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}
