package com.robybp.mytransferapp.navigation

interface Router {

    fun goToHomeScreen()

    fun goToDriversMenu()

    fun goToMeansOfTransport(transferType: String, apartmentName: String)

    fun goToNewGuestAirplaneBus(meansOfTransport: String, typeOfTransfer: String, apartmentName: String)

    fun goToNewGuestShipTrain(meansOfTransport: String, typeOfTransfer: String, apartmentName: String)

    fun goToPickDriver()

    fun goToGuestInfoPlane(guestId: Int)

    fun goToGuestInfoBus(guestId: Int)

    fun goToGuestInfoTrain(guestId: Int)

    fun goToGuestInfoShip(guestId: Int)

    fun showDatePickerDialog()

    fun showTimePickerDialog()

    fun goToApartmentsMenu()

    fun goToTransferType(apartmentName: String)

    fun goToNewApartment()

    fun returnToHomeScreen()

    fun goBack()
}
