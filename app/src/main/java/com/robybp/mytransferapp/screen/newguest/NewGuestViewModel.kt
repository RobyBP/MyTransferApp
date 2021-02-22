package com.robybp.mytransferapp.screen.newguest

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewGuestViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource
) : ViewModel() {

    fun crucialFieldsEmpty(fields: List<EditText>): Boolean {
        for (field in fields) {
            if (field.text.isNullOrEmpty()) {
                return true
            }
        }
        return false
    }

    fun saveGuest(guest: Guest): Job {
        return viewModelScope.launch(Dispatchers.IO) { repository.saveGuest(guest) }
    }

    fun getDriverByName(name: String): Maybe<Driver> =
        repository.getDriver(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun showDatePicker() = routingActionsSource.dispatch(Router::showDatePickerDialog)

    fun showTimePicker() = routingActionsSource.dispatch(Router::showTimePickerDialog)

    fun goToPickDriverFragment() = routingActionsSource.dispatch(Router::goToPickDriver)

    fun goToHomeScreen() = routingActionsSource.dispatch(Router::goToHomeScreen)

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun sendMessage(messageBody: String, phoneNumber: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, messageBody, null, null)
    }
}