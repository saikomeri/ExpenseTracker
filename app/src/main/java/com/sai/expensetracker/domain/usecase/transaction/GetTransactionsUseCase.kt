package com.sai.expensetracker.domain.usecase.transaction

import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(
        type: TransactionType? = null,
        startDate: Long? = null,
        endDate: Long? = null
    ): Flow<List<Transaction>> {
        return when {
            type != null && startDate != null && endDate != null ->
                repository.getTransactionsByTypeAndDateRange(type, startDate, endDate)
            type != null ->
                repository.getTransactionsByType(type)
            startDate != null && endDate != null ->
                repository.getTransactionsByDateRange(startDate, endDate)
            else ->
                repository.getAllTransactions()
        }
    }
}
