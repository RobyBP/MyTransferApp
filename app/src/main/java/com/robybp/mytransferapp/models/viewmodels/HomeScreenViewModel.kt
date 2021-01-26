package com.robybp.mytransferapp.models.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.robybp.mytransferapp.db.GuestBookDatabase
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.models.datamodels.Guest
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    val allGuests: Flowable<List<Guest>>
    private val repository: GuestBookRepository
    private val compositeDisposable = CompositeDisposable()

    init {
        val dao = GuestBookDatabase.getDatabase(application)!!.guestBookDao()
        repository = GuestBookRepository(dao)
        allGuests = repository.allGuests
    }

//    private fun queryGuests() {
//        compositeDisposable.add(
//            repository.allGuests
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                    setGuests(it)
//                }
//        )
//    }
//
//    private fun setGuests(guests: List<Guest>){
//        this.allGuests = guests
//    }

    fun saveGuest(guest: Guest) = viewModelScope.launch(Dispatchers.IO) {
        repository.addGuest(guest)
    }
}
