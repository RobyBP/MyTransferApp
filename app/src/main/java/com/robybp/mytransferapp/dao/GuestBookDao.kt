package com.robybp.mytransferapp.dao

import androidx.room.*
import com.robybp.mytransferapp.datamodels.Driver
import com.robybp.mytransferapp.datamodels.Guest
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface GuestBookDao {

    @Query("SELECT * FROM guests ORDER BY dateOfArrival DESC")
    fun getAllGuests(): Flowable<List<Guest>>

    @Query("SELECT * FROM drivers")
    fun getAllDrivers(): Flowable<List<Driver>>

    @Query("SELECT * FROM guests WHERE guestId = :id")
    fun getGuest(id: Int): Single<Guest>

    @Query("SELECT * FROM drivers WHERE name = :name")
    fun getDriver(name: String): Single<Driver>

    @Insert
    suspend fun addGuest(guest: Guest)

    @Insert
    suspend fun addDriver(driver: Driver)

    @Delete
    suspend fun deleteGuest(guest: Guest)

    @Delete
    suspend fun deleteDriver(driver: Driver)

    @Update
    suspend fun updateGuestData(guest: Guest)

    @Update
    suspend fun updateDriverData(driver: Driver)

}
