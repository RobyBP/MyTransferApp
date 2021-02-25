package com.robybp.mytransferapp.navigation

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.robybp.mytransferapp.R
import com.robybp.mytransferapp.screen.apartmentsmenu.ApartmentsMenuFragment
import com.robybp.mytransferapp.screen.dateandtimeofarrival.datepicker.DatePickerFragment
import com.robybp.mytransferapp.screen.dateandtimeofarrival.timepicker.TimePickerFragment
import com.robybp.mytransferapp.screen.driversmenu.DriversMenuFragment
import com.robybp.mytransferapp.screen.guestinfo.airplanebus.GuestInfoAirplaneBusFragment
import com.robybp.mytransferapp.screen.guestinfo.shiptrain.GuestInfoShipTrainFragment
import com.robybp.mytransferapp.screen.home.HomeFragment
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransportFragment
import com.robybp.mytransferapp.screen.newapartment.NewApartmentFragment
import com.robybp.mytransferapp.screen.newguest.airplanebus.NewGuestAirplaneBusFragment
import com.robybp.mytransferapp.screen.newguest.shiptrain.NewGuestShipTrainFragment
import com.robybp.mytransferapp.screen.pickdriver.PickDriverFragment

private const val MAIN_CONTAINER = R.id.fl_main

class RouterImpl(private val fragmentManager: FragmentManager) : Router {

    override fun goToHomeScreen() {
        fragmentManager.beginTransaction().apply {
            replace(MAIN_CONTAINER, HomeFragment())
            commit()
        }
    }

    override fun goToDriversMenu() {
        fragmentManager.beginTransaction().apply {
            applySlideAnimation()
            add(MAIN_CONTAINER, DriversMenuFragment())
            addToBackStack(DriversMenuFragment.TAG)
            commit()
        }
    }

    override fun goToMeansOfTransport(transferType: String) {
        val bundle = Bundle()
        val meansOfTransportFragment = MeansOfTransportFragment()
        bundle.putString("TransferType", transferType)
        meansOfTransportFragment.arguments = bundle
        fragmentManager.beginTransaction().apply {
            applySlideAnimation()
            add(MAIN_CONTAINER, meansOfTransportFragment)
            addToBackStack(MeansOfTransportFragment.TAG)
            commit()
        }
    }

    override fun goToNewGuestAirplane() {
        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.AIRPLANE.toString())
            val newGuestAirplaneBusFragment = NewGuestAirplaneBusFragment()
            newGuestAirplaneBusFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newGuestAirplaneBusFragment)
            addToBackStack(NewGuestAirplaneBusFragment.TAG)
            commit()
        }
    }

    override fun goToNewGuestBus() {
        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.BUS.toString())
            val newGuestAirplaneBusFragment = NewGuestAirplaneBusFragment()
            newGuestAirplaneBusFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newGuestAirplaneBusFragment)
            addToBackStack(NewGuestAirplaneBusFragment.TAG)
            commit()
        }
    }

    override fun goToNewGuestTrain() {

        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.TRAIN.toString())
            val newShipTrainFragment = NewGuestShipTrainFragment()
            newShipTrainFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newShipTrainFragment)
            addToBackStack(NewGuestShipTrainFragment.TAG)
            commit()
        }
    }

    override fun goToNewGuestShip() {

        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.SHIP.toString())
            val newShipTrainFragment = NewGuestShipTrainFragment()
            newShipTrainFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newShipTrainFragment)
            addToBackStack(NewGuestShipTrainFragment.TAG)
            commit()
        }
    }

    override fun goToPickDriver() {
        fragmentManager.beginTransaction().apply {
            applySlideAnimation()
            add(MAIN_CONTAINER, PickDriverFragment())
            addToBackStack(PickDriverFragment.TAG)
            commit()
        }
    }

    override fun goToGuestInfoPlane(guestId: Int) {
        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.AIRPLANE.toString())
            bundle.putInt("GuestId", guestId)
            val newGuestAirplaneBusFragment = GuestInfoAirplaneBusFragment()
            newGuestAirplaneBusFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newGuestAirplaneBusFragment)
            addToBackStack(GuestInfoAirplaneBusFragment.TAG)
            commit()
        }
    }

    override fun goToGuestInfoBus(guestId: Int) {
        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.BUS.toString())
            bundle.putInt("GuestId", guestId)
            val newGuestAirplaneBusFragment = GuestInfoAirplaneBusFragment()
            newGuestAirplaneBusFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newGuestAirplaneBusFragment)
            addToBackStack(GuestInfoAirplaneBusFragment.TAG)
            commit()
        }
    }

    override fun goToGuestInfoTrain(guestId: Int) {
        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.TRAIN.toString())
            bundle.putInt("GuestId", guestId)
            val newShipTrainFragment = GuestInfoShipTrainFragment()
            newShipTrainFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newShipTrainFragment)
            addToBackStack(GuestInfoShipTrainFragment.TAG)
            commit()
        }
    }

    override fun goToGuestInfoShip(guestId: Int) {
        fragmentManager.beginTransaction().apply {
            val bundle = Bundle()
            bundle.putString("Vehicle", MeansOfTransport.SHIP.toString())
            bundle.putInt("GuestId", guestId)
            val newShipTrainFragment = GuestInfoShipTrainFragment()
            newShipTrainFragment.arguments = bundle
            applySlideAnimation()
            add(MAIN_CONTAINER, newShipTrainFragment)
            addToBackStack(GuestInfoShipTrainFragment.TAG)
            commit()
        }
    }

    override fun showDatePickerDialog() {
        DatePickerFragment().show(fragmentManager, DatePickerFragment.TAG)
    }

    override fun showTimePickerDialog() {
        TimePickerFragment().show(fragmentManager, TimePickerFragment.TAG)
    }

    override fun goToApartmentsMenu() {
        fragmentManager.beginTransaction().apply {
            applySlideAnimation()
            add(MAIN_CONTAINER, ApartmentsMenuFragment())
            addToBackStack(ApartmentsMenuFragment.TAG)
            commit()
        }
    }

    override fun goToNewApartment() {
        fragmentManager.beginTransaction().apply {
            applySlideAnimation()
            add(MAIN_CONTAINER, NewApartmentFragment())
            addToBackStack(NewApartmentFragment.TAG)
            commit()
        }
    }

    override fun goBack() {
        fragmentManager.popBackStack()
    }
}
