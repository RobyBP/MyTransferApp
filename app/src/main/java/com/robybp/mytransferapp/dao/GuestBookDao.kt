package com.robybp.mytransferapp.dao

import androidx.room.*
import com.robybp.mytransferapp.datamodels.Apartment
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.datamodels.Guest
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface GuestBookDao {

    @Query("SELECT * FROM guests ORDER BY dateOfArrival DESC")
    fun getAllGuests(): Flowable<List<Guest>>

    @Query("SELECT * FROM drivers")
    fun getAllDrivers(): Flowable<List<Driver>>

    @Query("SELECT * FROM apartments")
    fun getAllApartments(): Flowable<List<Apartment>>

    @Query("SELECT * FROM guests WHERE guestId = :id")
    fun getGuest(id: Int): Single<Guest>

    @Query("SELECT * FROM drivers WHERE name = :name")
    fun getDriver(name: String): Maybe<Driver>

    @Query("SELECT * FROM apartments WHERE address = :address")
    fun getApartment(address: String): Maybe<Apartment>

    @Insert
    suspend fun saveGuest(guest: Guest)

    @Insert
    suspend fun saveDriver(driver: Driver)

    @Insert
    suspend fun saveApartment(apartment: Apartment)

    @Delete
    suspend fun deleteGuest(guest: Guest)

    @Delete
    suspend fun deleteDriver(driver: Driver)

    @Query("DELETE FROM apartments WHERE id = :apartmentId")
    suspend fun deleteApartment(apartmentId: Int)

    @Update
    suspend fun updateGuestData(guest: Guest)

    @Update
    suspend fun updateApartmentData(apartment: Apartment)

}
