package com.robybp.mytransferapp.sms

import com.robybp.mytransferapp.db.repository.GuestBookRepository
import io.reactivex.schedulers.Schedulers

class FindPhoneNumberUseCase(private val repository: GuestBookRepository) {
    fun getDriverPhoneNumberByName(name: String) = repository.getDriver(name).subscribeOn(Schedulers.io())
}
