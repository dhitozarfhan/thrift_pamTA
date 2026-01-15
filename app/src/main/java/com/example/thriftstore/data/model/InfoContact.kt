package com.example.thriftstore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "info_contacts")
data class InfoContact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val phone: String,
    val email: String,
    val address: String
)
