package com.daksh2k.allenmac.data

import com.daksh2k.allenmac.domain.model.Expense
import com.daksh2k.allenmac.domain.model.User

object MockData {
    val users = listOf(
        User(1, "Alice"),
        User(2, "Bob"),
        User(3, "Charlie"),
    )

    val expenses = listOf(
        Expense(
            1,
            100.0,
            users[0],
            listOf(users[0], users[1]),
        ),
        Expense(
            2,
            50.0,
            users[1],
            listOf(users[1], users[2]),
        ),
        Expense(
            3,
            75.0,
            users[2],
            listOf(users[0], users[2]),
        ),
    )
}