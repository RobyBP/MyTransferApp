package com.robybp.mytransferapp.navigation

import java.util.*

class RoutingActionsMediator : RoutingActionsSource {

    private var activeConsumer: RoutingActionConsumer? = null
    private val actionsQueue = LinkedList<(Router) -> Unit>()

    override fun registerActiveConsumer(consumer: RoutingActionConsumer) {
        activeConsumer = consumer
        flushQueue()
    }

    override fun deregisterConsumer() {
        activeConsumer = null
    }

    override fun dispatch(routingAction: (Router) -> Unit) {
        activeConsumer?.onRoutingAction(routingAction) ?: actionsQueue.add(routingAction)
    }

    private fun flushQueue() {
        while (!actionsQueue.isEmpty()) {
            activeConsumer!!.onRoutingAction(actionsQueue.poll())
        }
    }
}