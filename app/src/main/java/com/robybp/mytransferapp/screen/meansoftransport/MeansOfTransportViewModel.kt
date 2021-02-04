package com.robybp.mytransferapp.screen.meansoftransport

import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource

class MeansOfTransportViewModel(private val routingActionsSource: RoutingActionsSource) :
    ViewModel() {

    fun goToNewGuestAirplaneFragment() = routingActionsSource.dispatch(Router::goToNewGuestAirplane)

    fun goToNewGuestBusFragment() = routingActionsSource.dispatch(Router::goToNewGuestBus)

    fun goToNewGuestTrainFragment() = routingActionsSource.dispatch(Router::goToNewGuestTrain)

    fun goToNewGuestShipFragment() = routingActionsSource.dispatch(Router::goToNewGuestShip)
}
