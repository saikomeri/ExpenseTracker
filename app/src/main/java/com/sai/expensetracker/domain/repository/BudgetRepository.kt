package com.sai.expensetracker.domain.repository

import com.sai.expensetracker.domain.model.Budget
import kotlinx.coroutines.flow.Flow

interface BudgetRepository {
    fun getBudgetsByMonthYear(month: Int, year: Int): Flow<List<Budget>>
    suspend fun setBudget(budget: Budget): Long
    suspend fun deleteBudget(id: Long)
}
