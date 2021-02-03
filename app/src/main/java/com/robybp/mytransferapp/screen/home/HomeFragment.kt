package com.robybp.mytransferapp.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.R
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject

class HomeFragment : Fragment() {

    private lateinit var driversMenuButton: View
    private lateinit var addNewGuestButton: Button
    private val model: HomeViewModel by inject()
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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerview.adapter = adapter

        compositeDisposable.add(model.guests.subscribe { adapter.setGuests(it) })

        driversMenuButton.setOnClickListener {
            model.goToDriversMenu()
        }

        addNewGuestButton.setOnClickListener {
            model.goToMeansOfTransport()
        }
    }

    private fun initialiseViews(view: View) {
        driversMenuButton = view.findViewById(R.id.homescreen_settings_icon)
        addNewGuestButton = view.findViewById(R.id.homescreen_addGuest_button)
        recyclerview = view.findViewById(R.id.homescreen_recyclerview)
    }

    override fun onDestroyView() {
        compositeDisposable.dispose()
        super.onDestroyView()
    }
}
