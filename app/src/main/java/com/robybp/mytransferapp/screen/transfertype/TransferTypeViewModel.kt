package com.robybp.mytransferapp.screen.transfertype

import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource

class TransferTypeViewModel(private val routingActionsSource: RoutingActionsSource) : ViewModel() {

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun goToMeansOfTransport(transferType: String) = routingActionsSource.dispatch {
        it.goToMeansOfTransport(transferType)
    }
}
