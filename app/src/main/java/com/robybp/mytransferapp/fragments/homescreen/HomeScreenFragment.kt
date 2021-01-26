package com.robybp.mytransferapp.fragments.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.robybp.mytransferapp.MeansOfTransport
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.fragments.driversmenuscreen.DriversMenuFragment
import com.robybp.mytransferapp.models.datamodels.Guest
import com.robybp.mytransferapp.models.viewmodels.HomeScreenViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeScreenFragment: Fragment() {

    private lateinit var driversMenuButton: View
    private lateinit var addNewGuestButton: Button
    private lateinit var model: HomeScreenViewModel
    private lateinit var recyclerview: RecyclerView
    private val compositeDisposable = CompositeDisposable()
    private val adapter = HomeScreenAdapter()

    companion object{
        val TAG = "HomeScreenFragment"
    }

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

        compositeDisposable.add(
            model.allGuests
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    adapter.setGuests(it)
                }
        )

        driversMenuButton.setOnClickListener {
            fragmentManager!!.beginTransaction().apply {
                replace(R.id.fl_main, DriversMenuFragment())
                addToBackStack(TAG)
                commit()
            }
        }

        addNewGuestButton.setOnClickListener{
            model.saveGuest(Guest(
                name = "Roby",
                countryOfArrival = "Zagreb",
                vehicleInfo = "123",
                meansOfTransport = MeansOfTransport.AIRPLANE.toString(),
                dateOfArrival = "02/08/2021",
                timeOfArrival = "12:00",
                driverName = "Marijan",
                note = "",
                daysUntilArrival = 3,
                guestId = 0,
                portOrStation = null
            ))
        }
    }

    private fun initialiseViews(view: View){
        driversMenuButton = view.findViewById(R.id.homescreen_settings_icon)
        addNewGuestButton = view.findViewById(R.id.homescreen_addGuest_button)
        recyclerview = view.findViewById(R.id.homescreen_recyclerview)
    }

    private fun initialiseViewModels(){
        model = ViewModelProvider(
            requireActivity(),
            ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
        ).get(
            HomeScreenViewModel::class.java
        )
    }
}
