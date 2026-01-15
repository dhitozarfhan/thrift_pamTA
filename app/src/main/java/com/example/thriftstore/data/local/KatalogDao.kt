package com.example.thriftstore.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.thriftstore.data.model.Katalog
import kotlinx.coroutines.flow.Flow

@Dao
interface KatalogDao {
    @Query("SELECT * FROM katalog")
    fun getAllKatalog(): Flow<List<Katalog>>

    @Query("SELECT * FROM katalog WHERE id = :id")
    suspend fun getKatalogById(id: Int): Katalog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKatalog(katalog: Katalog)

    @Update
    suspend fun updateKatalog(katalog: Katalog)

    @Delete
    suspend fun deleteKatalog(katalog: Katalog)
}
