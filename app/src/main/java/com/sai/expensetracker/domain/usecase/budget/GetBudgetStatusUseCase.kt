package com.sai.expensetracker.domain.usecase.budget

import com.sai.expensetracker.domain.model.Budget
import com.sai.expensetracker.domain.repository.BudgetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBudgetStatusUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<List<Budget>> {
        return repository.getBudgetsByMonthYear(month, year)
    }
}
