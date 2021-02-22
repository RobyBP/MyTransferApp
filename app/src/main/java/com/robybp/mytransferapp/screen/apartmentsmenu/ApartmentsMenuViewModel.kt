package com.robybp.mytransferapp.screen.apartmentsmenu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApartmentsMenuViewModel(private val repository: GuestBookRepository, private val routingActionsSource: RoutingActionsSource) : ViewModel() {

    val apartments: Flowable<List<Apartment>> = repository.allApartments
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    fun goToNewApartment() = routingActionsSource.dispatch(Router::goToNewApartment)

    fun saveGuest() = viewModelScope.launch(Dispatchers.IO) {
        repository.saveApartment(Apartment(
            id = 0,
            name = "Name",
            city = "Zagreb",
            address = "Kurekov Breg 7",
            owner = "Roby",
            ownerPhoneNumber = "0917815512"
        ))
    }
}
