package com.robybp.mytransferapp.screen.driversmenu

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DriversMenuViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {

    val allDrivers: Flowable<List<Driver>> = repository.allDrivers

    fun saveDriver(driver: Driver) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveDriver(driver)
    }

    fun deleteDriver(driver: Driver) = viewModelScope.launch(Dispatchers.IO) {
        repository.removeDriver(driver)
        removeDriverFromGuests(driver.name)
    }

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun noDuplicates(drivers: List<Driver>, name: String): Boolean {
        for (driver in drivers) {
            if (name == driver.name) {
                return false
            }
        }
        return true
    }

    @SuppressLint("CheckResult")
    private fun removeDriverFromGuests(name: String) {
        repository.allGuests
            .firstOrError()
            .map { list -> list.filter { guest -> guest.driverName == name } }
            .subscribeOn(Schedulers.io())
            .subscribe({ it.forEach(this::updateGuestDriver) }, { Log.e("Update", "Error updating guests", it) })
    }

    private fun updateGuestDriver(guest: Guest) = viewModelScope.launch(Dispatchers.IO) {
        guest.driverName = null
        repository.updateGuestData(guest)
    }
}
