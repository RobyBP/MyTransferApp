package com.robybp.mytransferapp.screen.guestinfo

import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import com.robybp.mytransferapp.notificationscheduler.ScheduleNotificationUseCase
import com.robybp.mytransferapp.sms.FindPhoneNumberUseCase
import com.robybp.mytransferapp.sms.FormatMessageUseCase
import com.robybp.mytransferapp.sms.SendSmsUseCase
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GuestInfoViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource,
    private val findPhoneNumberUseCase: FindPhoneNumberUseCase,
    private val formatMessageUseCase: FormatMessageUseCase,
    private val sendSmsUseCase: SendSmsUseCase,
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase
) : ViewModel() {

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

    @SuppressLint("CheckResult")
    fun sendMessage(guest: Guest) {
        findPhoneNumberUseCase.getDriverPhoneNumberByName(guest.driverName!!)
            .subscribe({
                sendSmsUseCase.sendMessage(formatMessageUseCase.formatMessage(guest), it.phoneNumber)
                updateGuest(guest)
                scheduleNotificationUseCase.sendNotification(guest.dateOfArrival)
                goBack()
            }) { Log.d("Driver", it.message!!) }
    }

    fun showDatePicker() = routingActionsSource.dispatch(Router::showDatePickerDialog)

    fun showTimePicker() = routingActionsSource.dispatch(Router::showTimePickerDialog)

    fun updateGuest(guest: Guest) = viewModelScope.launch(Dispatchers.IO) {
        scheduleNotificationUseCase.sendNotification(guest.dateOfArrival)
        repository.updateGuestData(guest)
        goBack()
    }

    fun goToPickApartmentFragment() = routingActionsSource.dispatch(Router::goToPickApartment)

    fun goToPickDriverFragment() = routingActionsSource.dispatch(Router::goToPickDriver)

    fun goBack() = routingActionsSource.dispatch(Router::goBack)
}