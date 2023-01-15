package com.capstone.gometry.data.repository

import com.capstone.gometry.data.model.User
import com.capstone.gometry.utils.Response

interface FirebaseUserRepository {
    fun addUser(user: User, response: (Response<Pair<User, String>>) -> Unit)

    fun getUsers(response: (Response<List<User>>) -> Unit)

    fun getCurrentUser(response: (Response<User?>) -> Unit)

    fun updateCurrentUser(updatedData: Map<String, Any>, response: (Response<String>) -> Unit)
}