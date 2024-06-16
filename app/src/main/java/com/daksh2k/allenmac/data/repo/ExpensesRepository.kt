package com.daksh2k.allenmac.data.repo

import com.daksh2k.allenmac.data.MockData
import com.daksh2k.allenmac.domain.model.Expense

object ExpensesRepository {
    private val expenses = MockData.expenses.toMutableList()

    fun addExpense(expense: Expense) {
        expenses.add(expense)
    }

    fun getExpenses(): List<Expense> {
        return expenses
    }
}