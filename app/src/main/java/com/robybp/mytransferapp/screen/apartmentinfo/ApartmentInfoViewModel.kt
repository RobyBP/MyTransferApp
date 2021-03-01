package com.robybp.mytransferapp.screen.apartmentinfo

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ApartmentInfoViewModel(private val repository: GuestBookRepository, private val routingActionsSource: RoutingActionsSource) : ViewModel() {

    fun queryApartmentByAddress(address: String): Maybe<Apartment> {
        return repository.getApartment(address)
            .subscribeOn(Schedulers.io())
    }

    fun updateApartment(apartment: Apartment) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateApartmentData(apartment)
    }

    fun crucialFieldsEmpty(inputFields: List<EditText>): Boolean {
        for (field in inputFields) {
            if (field.text.isNullOrEmpty()) return true
        }
        return false
    }

    fun goToPickApartment() = routingActionsSource.dispatch(Router::goToPickApartment)

    fun deleteApartment(apartmentId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteApartment(apartmentId)
    }

    fun goBack() = routingActionsSource.dispatch(Router::goBack)
}
