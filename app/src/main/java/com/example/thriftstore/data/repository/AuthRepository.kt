package com.example.thriftstore.data.repository

import com.example.thriftstore.data.local.AdminDao
import com.example.thriftstore.data.local.UserDao
import com.example.thriftstore.data.model.Admin
import com.example.thriftstore.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val userDao: UserDao, private val adminDao: AdminDao) {

    suspend fun loginUser(email: String): User? = withContext(Dispatchers.IO) {
        userDao.getUserByEmail(email)
    }

    suspend fun loginAdmin(email: String): Admin? = withContext(Dispatchers.IO) {
        adminDao.getAdminByEmail(email)
    }

    suspend fun registerUser(user: User) = withContext(Dispatchers.IO) {
        userDao.insertUser(user)
    }
}
