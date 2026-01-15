package com.example.thriftstore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Katalog::class, parentColumns = ["id"], childColumns = ["katalog_id"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [
        androidx.room.Index("user_id"),
        androidx.room.Index("katalog_id")
    ]
)
data class CartItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user_id: Int,
    val katalog_id: Int,
    val quantity: Int,
    val created_at: Long = System.currentTimeMillis(),
    val updated_at: Long = System.currentTimeMillis()
)
