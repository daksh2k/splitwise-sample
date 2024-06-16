package com.daksh2k.allenmac.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daksh2k.allenmac.data.MockData.expenses
import com.daksh2k.allenmac.data.MockData.users
import com.daksh2k.allenmac.data.repo.ExpensesRepository
import com.daksh2k.allenmac.data.repo.UserRepository
import com.daksh2k.allenmac.domain.model.Expense
import com.daksh2k.allenmac.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    val events: Channel<MainEvent> = Channel(Channel.UNLIMITED)

    private val _uiState = MutableStateFlow(MainState())
    val uiState = _uiState.asStateFlow()


    init {
        handleEvents()
    }

    private fun handleEvents() {
        viewModelScope.launch {
            events.receiveAsFlow().collect { event ->
                when (event) {
                    is MainEvent.LoadData -> {
                        loadData()
                    }

                    is MainEvent.AddExpense -> {
                        addExpense(event.expense)
                    }
                }
            }
        }
    }

    private fun loadData() {

        _uiState.value = MainState(
            users = UserRepository.getUsers(), // get users from repository
            expenses = ExpensesRepository.getExpenses(), // get expenses from repository
            balances = calculateBalances(users, expenses)
        )
    }

    private fun addExpense(expense: Expense) {
        val updatedExpenses = _uiState.value.expenses.toMutableList()
        ExpensesRepository.addExpense(expense)
        updatedExpenses.add(expense)
        _uiState.value = _uiState.value.copy(
            expenses = updatedExpenses,
            balances = calculateBalances(_uiState.value.users, updatedExpenses),
        )

    }

    private fun calculateBalances(users: List<User>, expenses: List<Expense>): Map<User, Double> {
        val balances = mutableMapOf<User, Double>()

        users.forEach { user ->
            val owed = expenses.filter { it.participants.contains(user) }
                .sumOf { it.amount / it.participants.size }
            val paid = expenses.filter { it.payer == user }
                .sumOf { it.amount }
            balances[user] = paid - owed

        }

        return balances

    }

}