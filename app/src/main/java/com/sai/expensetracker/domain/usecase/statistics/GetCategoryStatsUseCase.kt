package com.sai.expensetracker.domain.usecase.statistics

import com.sai.expensetracker.domain.model.CategoryExpense
import com.sai.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoryStatsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(startDate: Long, endDate: Long): Flow<List<CategoryExpense>> {
        return repository.getCategoryExpenses(startDate, endDate)
    }
}
