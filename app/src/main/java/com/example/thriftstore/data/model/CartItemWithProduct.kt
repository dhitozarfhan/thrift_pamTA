package com.example.thriftstore.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CartItemWithProduct(
    @Embedded val cartItem: CartItem,
    @Relation(
        parentColumn = "katalog_id",
        entityColumn = "id"
    )
    val product: Katalog
)
