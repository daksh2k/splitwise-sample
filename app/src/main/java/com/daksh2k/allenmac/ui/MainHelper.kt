package com.daksh2k.allenmac.ui

import com.daksh2k.allenmac.domain.model.Expense
import com.daksh2k.allenmac.domain.model.User

sealed class MainEvent {
    data object LoadData : MainEvent()
    data class AddExpense(val expense: Expense) : MainEvent()
}

data class MainState(
    val expenses: List<Expense> = emptyList(),
    val users: List<User> = emptyList(),
    val balances: Map<User, Double> = emptyMap()
)