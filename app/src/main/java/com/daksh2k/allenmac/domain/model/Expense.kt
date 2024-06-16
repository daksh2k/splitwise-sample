package com.daksh2k.allenmac.domain.model

data class Expense(
    val id: Int,
    val amount: Double,
    val payer: User,
    val participants: List<User>,
)
