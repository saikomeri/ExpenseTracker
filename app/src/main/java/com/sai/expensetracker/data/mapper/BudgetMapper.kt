package com.sai.expensetracker.data.mapper

import com.sai.expensetracker.data.local.entity.BudgetEntity
import com.sai.expensetracker.data.local.entity.BudgetWithCategory
import com.sai.expensetracker.domain.model.Budget

fun BudgetWithCategory.toDomain(spent: Double = 0.0): Budget {
    return Budget(
        id = budget.id,
        categoryId = budget.categoryId,
        categoryName = category.name,
        categoryIcon = category.icon,
        categoryColor = category.color,
        amount = budget.amount,
        spent = spent,
        month = budget.month,
        year = budget.year
    )
}

fun Budget.toEntity(): BudgetEntity {
    return BudgetEntity(
        id = id,
        categoryId = categoryId,
        amount = amount,
        month = month,
        year = year
    )
}
