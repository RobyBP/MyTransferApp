package com.robybp.mytransferapp.screen.pickapartment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PickApartmentViewModel(private val repository: GuestBookRepository, private val routingActionsSource: RoutingActionsSource) : ViewModel() {

    val apartments: Flowable<List<Apartment>> = repository.allApartments
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    private val apartmentName = MutableLiveData<String>()

    fun setApartmentName(name: String?) {
        apartmentName.value = name
    }

    fun getApartmentName() = apartmentName

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun goToCreateNewApartment() = routingActionsSource.dispatch(Router::goToNewApartment)
}
