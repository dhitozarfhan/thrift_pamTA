package com.example.thriftstore

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.thriftstore.data.local.AppDatabase
import com.example.thriftstore.data.model.Admin
import com.example.thriftstore.data.model.Katalog
import com.example.thriftstore.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ThriftApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    private val applicationScope = CoroutineScope(Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "thrift_db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    applicationScope.launch {
                        if (database.adminDao().getAdminCount() == 0) {
                            database.userDao().insertUser(
                                User(
                                    name = "Customer Demo",
                                    email = "user@demo.com",
                                    phone = "081234567890",
                                    password = "password"
                                )
                            )

                            database.adminDao().insertAdmin(
                                Admin(
                                    name = "Admin Thrift",
                                    email = "admin@thrift.com",
                                    phone = "081122334455",
                                    password = "admin"
                                )
                            )

                            database.katalogDao().insertKatalog(
                                Katalog(name = "Vintage Denim Jacket", price = 150000.0, description = "Jacket denim klasik berkualitas tinggi dengan aksen vintage yang otentik.", image_url = "https://images.unsplash.com/photo-1576905355162-727aee607730?auto=format&fit=crop&q=80&w=800", is_active = true)
                            )
                            database.katalogDao().insertKatalog(
                                Katalog(name = "Flannel Shirt Red", price = 85000.0, description = "Kemeja flanel merah motif kotak-kotak, bahan lembut dan nyaman.", image_url = "https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?auto=format&fit=crop&q=80&w=800", is_active = true)
                            )
                            database.katalogDao().insertKatalog(
                                Katalog(name = "Levi's 501 Blue", price = 250000.0, description = "Celana jeans Levi's original pre-loved dalam kondisi sangat baik.", image_url = "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&q=80&w=800", is_active = true)
                            )
                            database.katalogDao().insertKatalog(
                                Katalog(name = "Retro Sneakers", price = 350000.0, description = "Sneakers retro klasik, desain timeless yang cocok untuk gaya apapun.", image_url = "https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&q=80&w=800", is_active = true)
                            )
                            database.katalogDao().insertKatalog(
                                Katalog(name = "Vintage Gold Watch", price = 750000.0, description = "Jam tangan vintage berlapis emas, menambah kesan elegan pada penampilan Anda.", image_url = "https://images.unsplash.com/photo-1524592094714-0f0654e20314?auto=format&fit=crop&q=80&w=800", is_active = true)
                            )
                        }
                    }
                }
            })
            .build()
    }
}
