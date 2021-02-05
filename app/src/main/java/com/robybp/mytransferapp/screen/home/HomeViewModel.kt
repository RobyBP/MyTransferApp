package com.robybp.mytransferapp.screen.home

import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import com.robybp.mytransferapp.screen.meansoftransport.MeansOfTransport
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {

    val guests: Flowable<List<Guest>> = repository.allGuests
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun goToMeansOfTransport() = routingActionsSource.dispatch(Router::goToMeansOfTransport)

    fun goToDriversMenu() = routingActionsSource.dispatch(Router::goToDriversMenu)

    fun goToGuestInfo(vehicle: String){
        when(vehicle){
            MeansOfTransport.BUS.toString() -> routingActionsSource.dispatch(Router::goToGuestInfoBus)
            MeansOfTransport.AIRPLANE.toString() -> routingActionsSource.dispatch(Router::goToGuestInfoPlane)
            MeansOfTransport.SHIP.toString() -> routingActionsSource.dispatch(Router::goToGuestInfoShip)
            MeansOfTransport.TRAIN.toString() -> routingActionsSource.dispatch(Router::goToGuestInfoTrain)
        }
    }
}
