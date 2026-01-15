package com.example.thriftstore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [androidx.room.Index("user_id")]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user_id: Int,
    val order_date: Long = System.currentTimeMillis(),
    val estimated_arrival: Long? = null,
    val status: String, // "menunggu", "diproses", "dikirim", "selesai", "dibatalkan"
    val total_amount: Double,
    val note: String? = null,
    val created_at: Long = System.currentTimeMillis(),
    val updated_at: Long = System.currentTimeMillis()
)
