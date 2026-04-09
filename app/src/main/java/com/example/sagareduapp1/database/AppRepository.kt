package com.example.sagareduapp1.database

import kotlinx.coroutines.flow.Flow

class AppRepository(private val dao: AppDao) {
    val allUsers: Flow<List<User>> = dao.getAllUsers()

    suspend fun insertUser(user: User) {
        dao.insert(user)
    }

    suspend fun deleteAllUsers() {
        dao.deleteAll()
    }
}
