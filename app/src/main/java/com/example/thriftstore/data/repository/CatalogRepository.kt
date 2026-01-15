package com.example.thriftstore.data.repository

import com.example.thriftstore.data.local.KatalogDao
import com.example.thriftstore.data.model.Katalog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class CatalogRepository(private val katalogDao: KatalogDao) {

    val allKatalog: Flow<List<Katalog>> = katalogDao.getAllKatalog().flowOn(Dispatchers.IO)

    suspend fun insertKatalog(katalog: Katalog) = withContext(Dispatchers.IO) {
        katalogDao.insertKatalog(katalog)
    }

    suspend fun deleteKatalog(katalog: Katalog) = withContext(Dispatchers.IO) {
        katalogDao.deleteKatalog(katalog)
    }

    suspend fun updateKatalog(katalog: Katalog) = withContext(Dispatchers.IO) {
        katalogDao.updateKatalog(katalog)
    }
    
    suspend fun getKatalogById(id: Int): Katalog? = withContext(Dispatchers.IO) {
        katalogDao.getKatalogById(id)
    }
}
