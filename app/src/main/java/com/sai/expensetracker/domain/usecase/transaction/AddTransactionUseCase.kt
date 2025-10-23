package com.sai.expensetracker.domain.usecase.transaction

import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction): Long {
        require(transaction.amount > 0) { "Amount must be greater than 0" }
        require(transaction.categoryId > 0) { "Category must be selected" }
        return repository.insertTransaction(transaction)
    }
}
