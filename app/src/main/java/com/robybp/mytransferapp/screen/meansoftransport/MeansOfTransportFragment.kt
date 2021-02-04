package com.robybp.mytransferapp.screen.meansoftransport

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MeansOfTransportFragment() : Fragment() {

    companion object {
        val TAG = "MeansOfTransportFragment"
    }

    private lateinit var busButton: Button
    private lateinit var trainButton: Button
    private lateinit var airplaneButton: Button
    private lateinit var shipButton: Button
    private val model: MeansOfTransportViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_means_of_transport, container, false);
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        busButton.setOnClickListener {
            model.goToNewGuestBusFragment()
        }

        trainButton.setOnClickListener {

        }

        airplaneButton.setOnClickListener {
            model.goToNewGuestAirplaneFragment()
        }

        shipButton.setOnClickListener {

        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialiseViews(view: View) {
        busButton = view.findViewById(R.id.meansoftransportscreen_bus_button)
        trainButton = view.findViewById(R.id.meansoftransportscreen_train_button)
        airplaneButton = view.findViewById(R.id.meansoftransportscreen_plane_button)
        shipButton = view.findViewById(R.id.meansoftransportscreen_ship_button)
    }
}