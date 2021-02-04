package com.robybp.mytransferapp.screen.newguest.shiptrain

import android.telephony.SmsManager
import android.widget.EditText
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

class NewGuestShipTrainViewModel(
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

    fun getDriverByName(name: String) =
        repository.getDriver(name)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun saveGuest(guest: Guest) = viewModelScope.launch(Dispatchers.IO) {
        repository.addGuest(guest)
    }

    fun showDatePicker() = routingActionsSource.dispatch(Router::showDatePickerDialog)

    fun showTimePicker() = routingActionsSource.dispatch(Router::showTimePickerDialog)

    fun goToPickDriverFragment() = routingActionsSource.dispatch(Router::goToPickDriver)

    fun goToHomeScreen() = routingActionsSource.dispatch(Router::goToHomeScreen)

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun sendMessage(phoneNumber: String, messageBody: String){
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, messageBody, null, null);
    }

}
