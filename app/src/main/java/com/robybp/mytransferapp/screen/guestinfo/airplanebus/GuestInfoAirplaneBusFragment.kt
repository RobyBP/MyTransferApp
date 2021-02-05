package com.robybp.mytransferapp.screen.guestinfo.airplanebus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport

class GuestInfoAirplaneBusFragment : Fragment() {

    companion object {
        const val TAG = "GuestInfoAirplaneBusFragment"
    }

    private lateinit var flightNumberOrBusCompanyText: TextView
    private lateinit var bottomIcon: ImageView
    private lateinit var topIcon: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialiseViews(view: View) {
        flightNumberOrBusCompanyText =
            view.findViewById(R.id.newguestbusairplanescreen_flightNumberOrBusCompany_hint)
        bottomIcon = view.findViewById(R.id.newguestbusairplanescreen_icon_bottom)
        topIcon = view.findViewById(R.id.newguestbusairplanescreen_top_icon)
    }
}
