package com.robybp.mytransferapp.navigation

interface Router {

    fun goToHomeScreen()

    fun goToDriversMenu()

    fun goToMeansOfTransport()

    fun goToNewGuestAirplane()

    fun goToNewGuestBus()

    fun goToNewGuestTrain()

    fun goToNewGuestShip()

    fun goGuestInfoAirplaneBus()

    fun goToGuestInfoTrainShip()

    fun showDatePickerDialog()

    fun showTimePickerDialog()

    fun goBack()
}
