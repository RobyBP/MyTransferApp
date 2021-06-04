package com.robybp.mytransferapp.screen.pickdriver

import android.telephony.SmsManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PickDriverViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {
    private val name = MutableLiveData<String?>()
    val allDrivers: Flowable<List<Driver>> = repository.allDrivers
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun setName(driverName: String?){
        name.value = driverName
    }

    fun sendMessage(messageBody: String, phoneNumber: String){
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, messageBody, null, null)
    }

    fun getName() = name

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun noDuplicates(drivers: List<Driver>, name: String): Boolean {
        for (driver in drivers) {
            if (name == driver.name) {
                return false
            }
        }
        return true
    }

    fun saveDriver(driver: Driver) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveDriver(driver)
    }
}
