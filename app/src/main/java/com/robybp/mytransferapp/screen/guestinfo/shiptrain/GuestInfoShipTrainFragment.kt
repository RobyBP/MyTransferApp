package com.robybp.mytransferapp.screen.guestinfo.shiptrain

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
import com.robybp.mytransferapp.screen.guestinfo.GuestInfoViewModel
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import com.robybp.mytransferapp.screen.pickapartment.PickApartmentViewModel
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
    private val sharedPickApartmentViewModel: PickApartmentViewModel by sharedViewModel()
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
    private lateinit var transferTypeHint: TextView
    private lateinit var apartmentNameEditText: EditText
    private lateinit var sendInfoButton: View
    private var guestId: Int? = null
    private var driverName: String? = null
    private var driverNotified: Boolean? = null

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
        sharedPickApartmentViewModel.setApartmentName(null)
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
                driverName = it.driverName
                driverNotified = it.driverNotified
            }
            .subscribe()

        sharedPickDriverViewModel.getName().observe(viewLifecycleOwner,
            { driverEditText.setText(it) })

        sharedPickApartmentViewModel.getApartmentName().observe(viewLifecycleOwner,
            { apartmentNameEditText.setText(it) })

        dateOfArrivalEditText.setOnClickListener {
            model.showDatePicker()
        }

        arrivalTimeEditText.setOnClickListener {
            model.showTimePicker()
        }

        apartmentNameEditText.setOnClickListener {
            model.goToPickApartmentFragment()
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
                Toast.makeText(requireContext(), "No Fields can be empty", Toast.LENGTH_LONG)
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
            Toast.makeText(requireContext(), "No field can be empty", Toast.LENGTH_LONG)
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
            transferType = transferTypeHint.text.toString(),
            apartmentName = apartmentNameEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            driverNotified = true,
            portOrStation = portOrStationEditText.text.toString()
        )

        model.sendMessage(guest)
    }

    private fun updateGuest() {
        val guest = Guest(
            guestId!!,
            name = guestNameEditText.text.toString(),
            vehicleInfo = shipOrTrainNumberEditText.text.toString(),
            countryOfArrival = arrivesFromEditText.text.toString(),
            dateOfArrival = dateOfArrivalEditText.text.toString(),
            timeOfArrival = arrivalTimeEditText.text.toString(),
            transferType = transferTypeHint.text.toString(),
            driverName = driverEditText.text.toString(),
            apartmentName = apartmentNameEditText.text.toString(),
            meansOfTransport = requireArguments()["Vehicle"].toString(),
            driverNotified = if (driverName == driverEditText.text.toString()) driverNotified!! else false,
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
        val formatter = SimpleDateFormat("HH:mm")
        val time = "$hours:$minutes"
        val date = formatter.parse(time)
        sharedDateTimeViewModel.hours = hours
        sharedDateTimeViewModel.minutes = minutes
        arrivalTimeEditText.setText(formatter.format(date))
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
        apartmentNameEditText = view.findViewById(R.id.newguestshiptrainscreen_transferType_editText)
        sendInfoButton = view.findViewById(R.id.newguestshiptrainscreen_sendinfo_button)
        transferTypeHint = view.findViewById(R.id.newguestshiptrainscreen_transferType_hint)

        driverEditText.inputType = EditorInfo.TYPE_NULL
        dateOfArrivalEditText.inputType = EditorInfo.TYPE_NULL
        arrivalTimeEditText.inputType = EditorInfo.TYPE_NULL
        apartmentNameEditText.inputType = EditorInfo.TYPE_NULL

        inputFields = listOf(
            guestNameEditText,
            shipOrTrainNumberEditText,
            portOrStationEditText,
            arrivesFromEditText,
            arrivalTimeEditText,
            dateOfArrivalEditText,
            driverEditText,
            apartmentNameEditText
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
        apartmentNameEditText.setText(guest.apartmentName)
        transferTypeHint.text = guest.transferType
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}
