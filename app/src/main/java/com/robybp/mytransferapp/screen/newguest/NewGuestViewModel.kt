package com.robybp.mytransferapp.screen.newguest

import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import com.robybp.mytransferapp.notificationscheduler.ScheduleNotificationUseCase
import com.robybp.mytransferapp.sms.FindPhoneNumberUseCase
import com.robybp.mytransferapp.sms.FormatMessageUseCase
import com.robybp.mytransferapp.sms.SendSmsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewGuestViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource,
    private val findPhoneNumberUseCase: FindPhoneNumberUseCase,
    private val formatMessageUseCase: FormatMessageUseCase,
    private val sendSmsUseCase: SendSmsUseCase,
    private val scheduleNotificationUseCase: ScheduleNotificationUseCase
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
        scheduleNotificationUseCase.sendNotification(guest.dateOfArrival)
        return viewModelScope.launch(Dispatchers.IO) { repository.saveGuest(guest) }
    }

    @SuppressLint("CheckResult")
    fun sendMessage(guest: Guest) {
        findPhoneNumberUseCase.getDriverPhoneNumberByName(guest.driverName!!)
            .subscribe({
                sendSmsUseCase.sendMessage(formatMessageUseCase.formatMessage(guest), it.phoneNumber)
                saveGuest(guest)
                scheduleNotificationUseCase.sendNotification(guest.dateOfArrival)
                goToHomeScreen()
            }) { Log.d("Driver", it.message!!) }
    }

    fun showDatePicker() = routingActionsSource.dispatch(Router::showDatePickerDialog)

    fun showTimePicker() = routingActionsSource.dispatch(Router::showTimePickerDialog)

    fun goToPickDriverFragment() = routingActionsSource.dispatch(Router::goToPickDriver)

    fun goToHomeScreen() = routingActionsSource.dispatch(Router::goToHomeScreen)

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun returnToHOmeScreen() = routingActionsSource.dispatch(Router::returnToHomeScreen)

    fun goToPickApartment() = routingActionsSource.dispatch(Router::goToPickApartment)
}