package com.example.thriftstore.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class OrderItemWithProduct(
    @Embedded val orderItem: OrderItem,
    @Relation(
        parentColumn = "katalog_id",
        entityColumn = "id"
    )
    val product: Katalog?
)
