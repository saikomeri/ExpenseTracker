package com.sai.expensetracker.domain.model

data class Budget(
    val id: Long = 0,
    val categoryId: Long,
    val categoryName: String = "",
    val categoryIcon: String = "",
    val categoryColor: Long = 0L,
    val amount: Double,
    val spent: Double = 0.0,
    val month: Int,
    val year: Int
) {
    val remaining: Double get() = amount - spent
    val progress: Float get() = if (amount > 0) (spent / amount).toFloat().coerceIn(0f, 1.5f) else 0f
    val isOverBudget: Boolean get() = spent > amount
    val isNearLimit: Boolean get() = progress >= 0.8f && !isOverBudget
}
