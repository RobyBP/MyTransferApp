package com.robybp.mytransferapp.screen.driversmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DriversMenuViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {

    val allDrivers: Flowable<List<Driver>> = repository.allDrivers

    fun saveDriver(driver: Driver) = viewModelScope.launch(Dispatchers.IO) {
        repository.addDriver(driver)
    }

    fun deleteDriver(driver: Driver) = viewModelScope.launch(Dispatchers.IO) {
        repository.removeDriver(driver)
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
}
