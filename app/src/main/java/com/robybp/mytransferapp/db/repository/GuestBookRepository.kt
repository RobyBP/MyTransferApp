package com.robybp.mytransferapp.db.repository

import com.robybp.mytransferapp.dao.GuestBookDao
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.datamodels.Guest
import io.reactivex.Flowable
import io.reactivex.Maybe

class GuestBookRepository(private val guestBookDao: GuestBookDao) {

    val allGuests: Flowable<List<Guest>> = guestBookDao.getAllGuests()
    val allDrivers: Flowable<List<Driver>> = guestBookDao.getAllDrivers()
    val allApartments: Flowable<List<Apartment>> = guestBookDao.getAllApartments()

    fun getGuest(id: Int) = guestBookDao.getGuest(id)

    fun getDriver(name: String): Maybe<Driver> = guestBookDao.getDriver(name)

    fun getApartment(address: String): Maybe<Apartment> = guestBookDao.getApartment(address)

    suspend fun saveGuest(guest: Guest) = guestBookDao.saveGuest(guest)

    suspend fun saveDriver(driver: Driver) = guestBookDao.saveDriver(driver)

    suspend fun saveApartment(apartment: Apartment) = guestBookDao.saveApartment(apartment)

    suspend fun updateApartmentData(apartment: Apartment) = guestBookDao.updateApartmentData(apartment)

    suspend fun updateGuestData(guest: Guest) = guestBookDao.updateGuestData(guest)

    suspend fun removeGuest(guest: Guest) = guestBookDao.deleteGuest(guest)

    suspend fun removeDriver(driver: Driver) = guestBookDao.deleteDriver(driver)

    suspend fun deleteApartment(apartment: Apartment) = guestBookDao.deleteApartment(apartment)
}
