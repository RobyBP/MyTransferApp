package com.robybp.mytransferapp.navigation

interface Router {

    fun goToHomeScreen()

    fun goToDriversMenu()

    fun goToMeansOfTransport()

    fun goToNewGuestAirplane()

    fun goToNewGuestBus()

    fun goToNewGuestTrain()

    fun goToNewGuestShip()

    fun goToPickDriver()

    fun goToGuestInfoPlane()

    fun goToGuestInfoBus()

    fun goToGuestInfoTrain()

    fun goToGuestInfoShip()

    fun showDatePickerDialog()

    fun showTimePickerDialog()

    fun goBack()
}
