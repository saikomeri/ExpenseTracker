package com.sai.expensetracker.domain.usecase.statistics

import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.repository.TransactionRepository
import com.sai.expensetracker.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import javax.inject.Inject

class GetSpendingTrendsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(startDate: Long, endDate: Long): Flow<Map<String, Double>> {
        return repository.getTransactionsByTypeAndDateRange(
            TransactionType.EXPENSE, startDate, endDate
        ).map { transactions ->
            transactions.groupBy { transaction ->
                DateUtils.formatDate(transaction.date, "MM/dd")
            }.mapValues { (_, txns) ->
                txns.sumOf { it.amount }
            }
        }
    }
}
