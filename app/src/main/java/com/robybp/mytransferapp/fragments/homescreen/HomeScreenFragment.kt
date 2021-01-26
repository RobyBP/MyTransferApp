package com.robybp.mytransferapp.fragments.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.fragments.driversmenuscreen.DriversMenuFragment

class HomeScreenFragment: Fragment() {

    private lateinit var driversMenuButton: View

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
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        driversMenuButton.setOnClickListener {
            fragmentManager!!.beginTransaction().apply {
                replace(R.id.fl_main, DriversMenuFragment())
                addToBackStack(TAG)
                commit()
            }
        }
        
    }

    private fun initialiseViews(view: View){
        driversMenuButton = view.findViewById(R.id.homescreen_settings_icon)
    }
}