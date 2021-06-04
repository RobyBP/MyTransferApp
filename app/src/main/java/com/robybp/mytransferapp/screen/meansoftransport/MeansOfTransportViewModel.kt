package com.robybp.mytransferapp.screen.meansoftransport

import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.navigation.RoutingActionsSource

class MeansOfTransportViewModel(private val routingActionsSource: RoutingActionsSource) :
    ViewModel() {

    fun goToNewGuestAirplaneBus(meansOfTransport: String, typeOfTransport: String, apartmentName: String) = routingActionsSource.dispatch {
        it.goToNewGuestAirplaneBus(meansOfTransport, typeOfTransport, apartmentName)
    }

    fun goToNewGuestShipTrain(meansOfTransport: String, typeOfTransport: String, apartmentName: String) = routingActionsSource.dispatch {
        it.goToNewGuestShipTrain(meansOfTransport, typeOfTransport, apartmentName)
    }
}
