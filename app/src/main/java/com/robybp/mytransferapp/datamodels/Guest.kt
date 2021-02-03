package com.robybp.mytransferapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "guests")
class Guest(
    @PrimaryKey(autoGenerate = true)
    val guestId: Int,
    val name: String,
    val vehicleInfo: String?,
    val countryOfArrival: String,
    val portOrStation: String?,
    val dateOfArrival: String,
    val timeOfArrival: String,
    val note: String?,
    var driverName: String?,
    val daysUntilArrival: Int,
    val meansOfTransport: String
)
