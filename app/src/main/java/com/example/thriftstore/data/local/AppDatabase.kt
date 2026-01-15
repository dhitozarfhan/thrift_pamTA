package com.example.thriftstore.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.thriftstore.data.model.Admin
import com.example.thriftstore.data.model.CartItem
import com.example.thriftstore.data.model.InfoContact
import com.example.thriftstore.data.model.Katalog
import com.example.thriftstore.data.model.Order
import com.example.thriftstore.data.model.OrderItem
import com.example.thriftstore.data.model.User

@Database(
    entities = [
        User::class,
        Admin::class,
        Katalog::class,
        CartItem::class,
        Order::class,
        OrderItem::class,
        InfoContact::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun adminDao(): AdminDao
    abstract fun katalogDao(): KatalogDao
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
}
