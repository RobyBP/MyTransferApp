package com.robybp.mytransferapp.navigation

import java.util.*

class RoutingActionsMediator : RoutingActionsSource {

    private var activeConsumer: RoutingActionConsumer? = null
    private val actionsQueue = LinkedList<QueuedAction>()

    override fun registerActiveConsumer(consumer: RoutingActionConsumer) {
        activeConsumer = consumer
    }

    override fun deregisterConsumer() {
        activeConsumer = null
    }

    override fun dispatch(routingAction: (Router) -> Unit) {
        actionsQueue.add(QueuedAction(routingAction))
        if (activeConsumer != null) flushQueue()
    }

    private fun flushQueue() {
        while (!actionsQueue.isEmpty()) {
            activeConsumer!!.onRoutingAction(actionsQueue.poll().routingAction)
        }
    }

    class QueuedAction(val routingAction: (Router) -> Unit)
}
