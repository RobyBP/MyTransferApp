package com.robybp.mytransferapp.screen.guestinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestInfoViewModel(private val repository: GuestBookRepository, private val routingActionsSource: RoutingActionsSource): ViewModel() {

    fun queryGuest(id: Int) =
        repository.getGuest(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun updateGuest(guest: Guest) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateGuestData(guest)
    }

    fun goBack() = routingActionsSource.dispatch(Router::goBack)
}