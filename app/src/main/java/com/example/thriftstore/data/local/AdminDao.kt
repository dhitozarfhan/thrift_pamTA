package com.example.thriftstore.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.thriftstore.data.model.Admin

@Dao
interface AdminDao {
    @Query("SELECT * FROM admins WHERE email = :email LIMIT 1")
    suspend fun getAdminByEmail(email: String): Admin?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAdmin(admin: Admin)
    
    @Query("SELECT COUNT(*) FROM admins")
    suspend fun getAdminCount(): Int
}
