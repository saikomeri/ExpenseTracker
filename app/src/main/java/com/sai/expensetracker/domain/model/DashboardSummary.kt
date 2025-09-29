package com.sai.expensetracker.domain.model

data class DashboardSummary(
    val totalBalance: Double = 0.0,
    val monthlyIncome: Double = 0.0,
    val monthlyExpense: Double = 0.0,
    val recentTransactions: List<Transaction> = emptyList(),
    val categoryExpenses: List<CategoryExpense> = emptyList()
)

data class CategoryExpense(
    val categoryId: Long,
    val categoryName: String,
    val categoryIcon: String,
    val categoryColor: Long,
    val totalAmount: Double,
    val percentage: Float = 0f
)
