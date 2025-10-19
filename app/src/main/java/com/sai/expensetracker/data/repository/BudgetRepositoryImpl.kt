package com.sai.expensetracker.data.repository

import com.sai.expensetracker.data.local.dao.BudgetDao
import com.sai.expensetracker.data.local.dao.TransactionDao
import com.sai.expensetracker.data.mapper.toDomain
import com.sai.expensetracker.data.mapper.toEntity
import com.sai.expensetracker.domain.model.Budget
import com.sai.expensetracker.domain.repository.BudgetRepository
import com.sai.expensetracker.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepositoryImpl @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) : BudgetRepository {

    override fun getBudgetsByMonthYear(month: Int, year: Int): Flow<List<Budget>> {
        return budgetDao.getBudgetsByMonthYear(month, year).map { budgetsWithCategory ->
            budgetsWithCategory.map { bwc ->
                val startOfMonth = getMonthStartTimestamp(month, year)
                val endOfMonth = getMonthEndTimestamp(month, year)
                val spent = transactionDao.getSpentByCategory(
                    bwc.budget.categoryId, startOfMonth, endOfMonth
                ).first()
                bwc.toDomain(spent = spent)
            }
        }
    }

    override suspend fun setBudget(budget: Budget): Long {
        return budgetDao.insertBudget(budget.toEntity())
    }

    override suspend fun deleteBudget(id: Long) {
        budgetDao.deleteBudget(id)
    }

    private fun getMonthStartTimestamp(month: Int, year: Int): Long {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, year)
            set(java.util.Calendar.MONTH, month - 1)
            set(java.util.Calendar.DAY_OF_MONTH, 1)
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun getMonthEndTimestamp(month: Int, year: Int): Long {
        val calendar = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.YEAR, year)
            set(java.util.Calendar.MONTH, month - 1)
            set(java.util.Calendar.DAY_OF_MONTH, getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
            set(java.util.Calendar.HOUR_OF_DAY, 23)
            set(java.util.Calendar.MINUTE, 59)
            set(java.util.Calendar.SECOND, 59)
            set(java.util.Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }
}
