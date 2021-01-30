package com.robybp.mytransferapp.models.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.db.GuestBookDatabase
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.models.datamodels.Driver
import io.reactivex.Flowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DriversMenuViewModel(application: Application): AndroidViewModel(application) {

    private val repository: GuestBookRepository
    val allDrivers: Flowable<List<Driver>>

    init {
        val dao = GuestBookDatabase.getDatabase(application)!!.guestBookDao()
        repository = GuestBookRepository(dao)
        allDrivers = repository.allDrivers
    }

    fun saveDriver(driver: Driver) = viewModelScope.launch(Dispatchers.IO) {
        repository.addDriver(driver)
    }
}
