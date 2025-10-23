package com.sai.expensetracker.domain.usecase.transaction

import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(id: Long): Transaction? {
        return repository.getTransactionById(id)
    }
}
