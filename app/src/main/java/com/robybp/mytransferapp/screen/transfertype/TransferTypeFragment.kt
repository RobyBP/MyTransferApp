package com.robybp.mytransferapp.screen.transfertype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransferTypeFragment : Fragment() {

    private lateinit var cancelButton: View
    private lateinit var fromApartmentButton: Button
    private lateinit var toApartmentButton: Button
    private val model: TransferTypeViewModel by viewModel()

    companion object {
        const val TAG = "TransferTypeFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_transfer_type, container, false)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        cancelButton.setOnClickListener {
            model.goBack()
        }

        fromApartmentButton.setOnClickListener {
            model.goToMeansOfTransport("From apartment: ", requireArguments().getString("Apartment")!!)
        }

        toApartmentButton.setOnClickListener {
            model.goToMeansOfTransport("To apartment:", requireArguments().getString("Apartment")!!)
        }

        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialiseViews(view: View) {
        cancelButton = view.findViewById(R.id.transfettype_close_icon)
        fromApartmentButton = view.findViewById(R.id.transfertype_fromApartment_button)
        toApartmentButton = view.findViewById(R.id.transfertype_toApartment_button)
    }
}