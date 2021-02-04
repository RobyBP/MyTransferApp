package com.robybp.mytransferapp.screen.pickdriver

import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PickDriverViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {

    val allDrivers = repository.allDrivers
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

}
