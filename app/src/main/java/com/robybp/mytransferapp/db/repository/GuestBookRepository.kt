package com.robybp.mytransferapp.db.repository

import com.robybp.mytransferapp.dao.GuestBookDao
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.datamodels.Guest
import io.reactivex.Flowable

class GuestBookRepository(private val guestBookDao: GuestBookDao) {

    val allGuests: Flowable<List<Guest>> = guestBookDao.getAllGuests()
    val allDrivers: Flowable<List<Driver>> = guestBookDao.getAllDrivers()

    fun getGuest(id: Int) = guestBookDao.getGuest(id)

    fun getDriver(name: String) = guestBookDao.getDriver(name)

    suspend fun addGuest(guest: Guest) = guestBookDao.addGuest(guest)

    suspend fun addDriver(driver: Driver) = guestBookDao.addDriver(driver)

    suspend fun updateGuestData(guest: Guest) = guestBookDao.updateGuestData(guest)

    suspend fun updateDriverData(driver: Driver) = guestBookDao.updateDriverData(driver)

    suspend fun removeGuest(guest: Guest) = guestBookDao.deleteGuest(guest)

    suspend fun removeDriver(driver: Driver) = guestBookDao.deleteDriver(driver)

}
