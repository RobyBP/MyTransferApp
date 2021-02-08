package com.robybp.mytransferapp.screen.guestinfo.airplanebus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.screen.guestinfo.GuestInfoViewModel
import com.robybp.mytransferapp.screen.home.HomeViewModel
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class GuestInfoAirplaneBusFragment : Fragment() {

    companion object {
        const val TAG = "GuestInfoAirplaneBusFragment"
    }

    private val compositeDisposable = CompositeDisposable()
    private val model: GuestInfoViewModel by viewModel()
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

    private var listOfInputFields = listOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new_guest_airplane_bus, container, false)
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

        model.queryGuest(requireArguments().getInt("GuestId"))
            .doOnSuccess{
                setInfo(it)
            }
            .subscribe()

        cancelButton.setOnClickListener {
            compositeDisposable.dispose()
            model.goBack()
        }
        super.onViewCreated(view, savedInstanceState)
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

    private fun setInfo(guest: Guest){
        nameEditText.setText(guest.name)
        flightNumberOrBusCompanyEditText.setText(guest.vehicleInfo)
        arrivesFromEditText.setText(guest.countryOfArrival)
        dateOfArrivalEditText.setText(guest.dateOfArrival)
        arrivalTimeEditText.setText(guest.timeOfArrival)
        driverEditText.setText(guest.driverName)
        noteEditText.setText(guest.note)
    }
}
