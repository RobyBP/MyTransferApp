package com.robybp.mytransferapp.models.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.robybp.mytransferapp.db.GuestBookDatabase
import com.robybp.mytransferapp.db.repository.GuestBookRepository
import com.robybp.mytransferapp.models.datamodels.Guest
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {

    var allGuests: List<Guest>? = null
    private val repository: GuestBookRepository
    private val compositeDisposable = CompositeDisposable()

    init {
        val dao = GuestBookDatabase.getDatabase(application)!!.guestBookDao()
        repository = GuestBookRepository(dao)
        queryGuests()
    }

    private fun queryGuests() {
        compositeDisposable.add(
            repository.allGuests
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    allGuests = it
                }
        )
    }

}
