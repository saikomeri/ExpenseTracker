package com.sai.expensetracker.domain.usecase.dashboard

import com.sai.expensetracker.domain.model.DashboardSummary
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.repository.TransactionRepository
import com.sai.expensetracker.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetDashboardSummaryUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<DashboardSummary> {
        val startOfMonth = DateUtils.getStartOfMonth()
        val endOfMonth = DateUtils.getEndOfMonth()

        return combine(
            repository.getTotalIncome(),
            repository.getTotalExpense(),
            repository.getTotalByTypeAndDateRange(TransactionType.INCOME, startOfMonth, endOfMonth),
            repository.getTotalByTypeAndDateRange(TransactionType.EXPENSE, startOfMonth, endOfMonth),
            repository.getRecentTransactions(5),
            repository.getCategoryExpenses(startOfMonth, endOfMonth)
        ) { values ->
            val totalIncome = values[0] as Double
            val totalExpense = values[1] as Double
            val monthlyIncome = values[2] as Double
            val monthlyExpense = values[3] as Double
            @Suppress("UNCHECKED_CAST")
            val recentTransactions = values[4] as List<com.sai.expensetracker.domain.model.Transaction>
            @Suppress("UNCHECKED_CAST")
            val categoryExpenses = values[5] as List<com.sai.expensetracker.domain.model.CategoryExpense>

            DashboardSummary(
                totalBalance = totalIncome - totalExpense,
                monthlyIncome = monthlyIncome,
                monthlyExpense = monthlyExpense,
                recentTransactions = recentTransactions,
                categoryExpenses = categoryExpenses
            )
        }
    }
}
