package com.robybp.mytransferapp.models.viewmodels

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.db.GuestBookDatabase
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.models.datamodels.Guest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewGuestAirplaneBusViewModel(application: Application): AndroidViewModel(application) {

    private val repository: GuestBookRepository

    init {
        val dao = GuestBookDatabase.getDatabase(application)!!.guestBookDao()
        repository = GuestBookRepository(dao)
    }
    fun crucialFieldsEmpty(fields: List<EditText>): Boolean{
        for(field in fields){
            if(field.text.isNullOrEmpty()){
                return true
            }
        }
        return false
    }

    fun saveGuest(guest: Guest) = viewModelScope.launch(Dispatchers.IO){
        repository.addGuest(guest)
    }
}
