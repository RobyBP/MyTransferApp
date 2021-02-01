package com.robybp.mytransferapp.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.fragments.driversmenuscreen.DriversMenuFragment
import com.robybp.mytransferapp.fragments.meansoftransportscreen.MeansOfTransportFragment
import io.reactivex.disposables.CompositeDisposable

class HomeFragment : Fragment() {

    private lateinit var driversMenuButton: View
    private lateinit var addNewGuestButton: Button
    private lateinit var model: HomeViewModel
    private lateinit var recyclerview: RecyclerView
    private val compositeDisposable = CompositeDisposable() // TODO: Leaking subscriptions
    private val adapter = HomeAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_screen, container, false)
        initialiseViews(view)
        initialiseViewModels()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.adapter = adapter

        compositeDisposable.add(model.guests.subscribe { adapter.setGuests(it) })

        driversMenuButton.setOnClickListener {
            fragmentManager!!.beginTransaction().apply {
                replace(R.id.fl_main, DriversMenuFragment())
                addToBackStack(DriversMenuFragment.TAG)
                commit()
            }
        }

        addNewGuestButton.setOnClickListener {

            fragmentManager!!.beginTransaction().apply {
                replace(R.id.fl_main, MeansOfTransportFragment())
                addToBackStack(MeansOfTransportFragment.TAG)
                commit()
            }
        }
    }

    private fun initialiseViews(view: View) {
        driversMenuButton = view.findViewById(R.id.homescreen_settings_icon)
        addNewGuestButton = view.findViewById(R.id.homescreen_addGuest_button)
        recyclerview = view.findViewById(R.id.homescreen_recyclerview)
    }

    private fun initialiseViewModels() {
        model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            HomeViewModel::class.java
        )
    }
}
