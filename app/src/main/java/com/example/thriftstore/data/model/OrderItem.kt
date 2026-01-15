package com.example.thriftstore.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(entity = Order::class, parentColumns = ["id"], childColumns = ["order_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Katalog::class, parentColumns = ["id"], childColumns = ["katalog_id"], onDelete = ForeignKey.SET_NULL)
    ],
    indices = [
        androidx.room.Index("order_id"),
        androidx.room.Index("katalog_id")
    ]
)
data class OrderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val order_id: Int,
    val katalog_id: Int?,
    val quantity: Int,
    val price_at_purchase: Double
)
