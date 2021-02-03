package com.robybp.mytransferapp.screen.newguest.airplanebus

import android.widget.EditText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.datamodels.Guest
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.navigation.Router
import com.robybp.mytransferapp.navigation.RoutingActionsSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewGuestAirplaneBusViewModel(
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

    fun saveGuest(guest: Guest) = viewModelScope.launch(Dispatchers.IO) {
        repository.addGuest(guest)
    }

    fun showDatePicker() = routingActionsSource.dispatch(Router::showDatePickerDialog)

    fun showTimePicker() = routingActionsSource.dispatch(Router::showTimePickerDialog)

    fun goBack() = routingActionsSource.dispatch(Router::goBack)
}
