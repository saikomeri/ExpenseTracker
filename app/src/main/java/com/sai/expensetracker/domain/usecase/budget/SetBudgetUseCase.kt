package com.sai.expensetracker.domain.usecase.budget

import com.sai.expensetracker.domain.model.Budget
import com.sai.expensetracker.domain.repository.BudgetRepository
import javax.inject.Inject

class SetBudgetUseCase @Inject constructor(
    private val repository: BudgetRepository
) {
    suspend operator fun invoke(budget: Budget): Long {
        require(budget.amount > 0) { "Budget amount must be greater than 0" }
        require(budget.categoryId > 0) { "Category must be selected" }
        return repository.setBudget(budget)
    }
}
