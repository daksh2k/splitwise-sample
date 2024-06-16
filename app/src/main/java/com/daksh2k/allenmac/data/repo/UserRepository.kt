package com.daksh2k.allenmac.data.repo

import com.daksh2k.allenmac.data.MockData
import com.daksh2k.allenmac.domain.model.User

object UserRepository {

    private val users = MockData.users.toMutableList()

    fun addUser(user: User) {
        users.add(user)
    }

    fun getUsers(): List<User> {
        return users
    }

}