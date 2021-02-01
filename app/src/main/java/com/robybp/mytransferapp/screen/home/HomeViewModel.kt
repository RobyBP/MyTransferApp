package com.robybp.mytransferapp.screen.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.db.GuestBookDatabase
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.models.datamodels.Guest
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GuestBookRepository

    val guests: Flowable<List<Guest>>

    init {
        val dao = GuestBookDatabase.getDatabase(application)!!.guestBookDao()
        repository = GuestBookRepository(dao)

        guests =
            repository.allGuests
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun saveGuest(guest: Guest) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGuest(guest)
        }
}
