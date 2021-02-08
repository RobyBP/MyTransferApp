package com.robybp.mytransferapp.screen.guestinfo.shiptrain

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
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import io.reactivex.disposables.CompositeDisposable
import org.koin.androidx.viewmodel.ext.android.viewModel

class GuestInfoShipTrainFragment: Fragment() {

    companion object{
        const val TAG = "GuestInfoShipTrainFragment"
    }

    private val compositeDisposable = CompositeDisposable()
    private val model: GuestInfoViewModel by viewModel()
    private lateinit var topIcon: ImageView
    private lateinit var bottomIcon: ImageView
    private lateinit var trainOrShipNumberHint: TextView
    private lateinit var portOrStationHint: TextView
    private lateinit var saveButton: View
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

    private var inputFields = listOf<EditText>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_new_guest_ship_train, container, false)
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

    private fun initialiseViews(view: View){
        topIcon = view.findViewById(R.id.newguestshiptrainscreen_top_icon)
        bottomIcon = view.findViewById(R.id.newguestshiptrainscreen_icon_bottom)
        trainOrShipNumberHint =
            view.findViewById(R.id.newguestshiptrainscreen_trainOrShipNumber_hint)
        portOrStationHint = view.findViewById(R.id.newguestshiptrainscreen_portOrStation_hint)
        saveButton = view.findViewById(R.id.newguestshiptrainscreen_save_button)
        cancelButton = view.findViewById(R.id.newguestshiptrainscreen_cancel_button)
        guestNameEditText = view.findViewById(R.id.newguestshiptrainscreen_guestName_editText)
        shipOrTrainNumberEditText =
            view.findViewById(R.id.newguestshiptrainscreen_trainOrShipNumber_editText)
        portOrStationEditText =
            view.findViewById(R.id.newguestshiptrainscreen_portOrStation_editText)
        arrivesFromEditText = view.findViewById(R.id.newguestshiptrainscreen_arrivesFrom_editText)
        dateOfArrivalEditText =
            view.findViewById(R.id.newguestshiptrainscreen_dateOfArrival_editText)
        arrivalTimeEditText = view.findViewById(R.id.newguestshiptrainscreen_timeOfArrival_editText)
        driverEditText = view.findViewById(R.id.newguestshiptrainscreen_driver_editText)
        noteEditText = view.findViewById(R.id.newguestshiptrainscreen_note_editText)
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

    private fun setInfo(guest: Guest){
        guestNameEditText.setText(guest.name)
        arrivesFromEditText.setText(guest.countryOfArrival)
        shipOrTrainNumberEditText.setText(guest.vehicleInfo)
        portOrStationEditText.setText(guest.portOrStation)
        dateOfArrivalEditText.setText(guest.dateOfArrival)
        arrivalTimeEditText.setText(guest.timeOfArrival)
        driverEditText.setText(guest.driverName)
        noteEditText.setText(guest.note)
    }
}
