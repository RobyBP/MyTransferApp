package com.robybp.mytransferapp.navigation

interface RoutingActionConsumer {

    fun onRoutingAction(routingAction: (Router) -> Unit)

}
