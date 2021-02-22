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

    fun goToGuestInfoPlane(guestId: Int)

    fun goToGuestInfoBus(guestId: Int)

    fun goToGuestInfoTrain(guestId: Int)

    fun goToGuestInfoShip(guestId: Int)

    fun showDatePickerDialog()

    fun showTimePickerDialog()

    fun goToApartmentsMenu()

    fun goBack()
}
