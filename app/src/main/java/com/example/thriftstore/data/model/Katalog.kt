package com.example.thriftstore.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "katalog")
data class Katalog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val description: String,
    val price: Double,
    val image_url: String,
    val is_active: Boolean = true,
    val created_at: Long = System.currentTimeMillis(),
    val updated_at: Long = System.currentTimeMillis()
)
