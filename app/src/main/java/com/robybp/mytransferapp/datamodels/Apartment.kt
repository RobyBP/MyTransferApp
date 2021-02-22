package com.robybp.mytransferapp.datamodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "apartments")
data class Apartment(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val city: String,
    val address: String,
    val owner: String,
    val ownerPhoneNumber: String
)
