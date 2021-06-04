package com.robybp.mytransferapp.navigation

interface RoutingActionsSource {

    fun registerActiveConsumer(consumer: RoutingActionConsumer)

    fun deregisterConsumer()

    fun dispatch(routingAction: (Router) -> Unit)

}