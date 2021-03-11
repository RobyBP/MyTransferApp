package com.robybp.mytransferapp.screen.newapartment

import android.annotation.SuppressLint
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import com.robybp.mytransferapp.sms.FindPhoneNumberUseCase
import com.robybp.mytransferapp.sms.FormatMessageUseCase
import com.robybp.mytransferapp.sms.SendSmsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewApartmentViewModel(
    private val repository: GuestBookRepository,
    private val routingActionsSource: RoutingActionsSource,
    private val sendSmsUseCase: SendSmsUseCase,
    private val formatMessageUseCase: FormatMessageUseCase,
    private val findPhoneNumberUseCase: FindPhoneNumberUseCase
) : ViewModel() {

    fun saveApartment(apartment: Apartment) = viewModelScope.launch(Dispatchers.IO) {
        repository.saveApartment(apartment)
    }

    @SuppressLint("CheckResult")
    fun sendMessage(apartment: Apartment, driverName: String) {
        findPhoneNumberUseCase.getDriverPhoneNumberByName(driverName)
            .subscribe({
                sendSmsUseCase.sendMessage(formatMessageUseCase.formatMessage(apartment), it.phoneNumber)
                saveApartment(apartment)
                goBack()
            }) { Log.d("Driver", it.message!!) }
    }

    fun goToPickDriverFragment() = routingActionsSource.dispatch(Router::goToPickDriver)

    fun goBack() = routingActionsSource.dispatch(Router::goBack)

    fun crucialFieldsEmpty(inputFields: List<EditText>): Boolean {
        for (field in inputFields) {
            if (field.text.isNullOrEmpty()) return true
        }
        return false
    }
}
