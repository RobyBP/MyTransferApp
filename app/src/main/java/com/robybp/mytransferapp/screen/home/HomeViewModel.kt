package com.robybp.mytransferapp.screen.home

import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
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
}
