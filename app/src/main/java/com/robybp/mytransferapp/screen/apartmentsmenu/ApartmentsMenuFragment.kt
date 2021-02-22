package com.robybp.mytransferapp.screen.apartmentsmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.robybp.mytransferapp.R

class ApartmentsMenuFragment : Fragment() {

    private lateinit var closeButton: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var newApartmentButton: FloatingActionButton

    companion object {
        const val TAG = "ApartmentsMenuFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_apartments_menu, container, false)
        initialiseViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initialiseViews(view: View) {
        closeButton = view.findViewById(R.id.apartmentsmenu_close_icon)
        recyclerView = view.findViewById(R.id.apartmentsmenu_recyclerview)
        newApartmentButton = view.findViewById(R.id.apartmentsmenu_addApartment_button)
    }
}
