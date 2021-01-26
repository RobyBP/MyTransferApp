package com.robybp.mytransferapp.models.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class Driver(
    @PrimaryKey(autoGenerate = true)
    val driverId: Int,
    val name: String,
    val phoneNumber: String
)
