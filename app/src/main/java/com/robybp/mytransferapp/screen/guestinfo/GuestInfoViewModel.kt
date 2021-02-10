package com.robybp.mytransferapp.screen.guestinfo

import android.telephony.SmsManager
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestInfoViewModel(private val repository: GuestBookRepository, private val routingActionsSource: RoutingActionsSource): ViewModel() {

    fun queryGuest(id: Int) =
        repository.getGuest(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun crucialFieldsEmpty(fields: List<EditText>): Boolean {
        for (field in fields) {
            if (field.text.isNullOrEmpty()) {
                return true
            }
        }
        return false
    }

    fun getDriverByName(name: String): Maybe<Driver> =
        repository.getDriver(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun sendMessage(messageBody: String, phoneNumber: String){
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, messageBody, null, null)
    }

    fun showDatePicker() = routingActionsSource.dispatch(Router::showDatePickerDialog)

    fun showTimePicker() = routingActionsSource.dispatch(Router::showTimePickerDialog)

    fun updateGuest(guest: Guest) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateGuestData(guest)
    }

    fun goBack() = routingActionsSource.dispatch(Router::goBack)
}