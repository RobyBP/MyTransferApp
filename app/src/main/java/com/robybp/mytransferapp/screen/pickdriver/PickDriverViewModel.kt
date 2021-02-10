package com.robybp.mytransferapp.screen.pickdriver

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PickDriverViewModel(
    repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {
    private val name = MutableLiveData<String>()
    val allDrivers: Flowable<List<Driver>> = repository.allDrivers
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun setName(driverName: String?){
        name.value = driverName
    }

    fun getName() = name

    fun goBack() = routingActionsSource.dispatch(Router::goBack)
}
