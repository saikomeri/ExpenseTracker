package com.sai.expensetracker.presentation.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.expensetracker.domain.model.Budget
import com.sai.expensetracker.domain.model.Category
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.repository.CategoryRepository
import com.sai.expensetracker.domain.usecase.budget.GetBudgetStatusUseCase
import com.sai.expensetracker.domain.usecase.budget.SetBudgetUseCase
import com.sai.expensetracker.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetUiState(
    val budgets: List<Budget> = emptyList(),
    val expenseCategories: List<Category> = emptyList(),
    val isLoading: Boolean = true,
    val showAddDialog: Boolean = false,
    val dialogCategoryId: Long = -1,
    val dialogAmount: String = ""
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val getBudgetStatus: GetBudgetStatusUseCase,
    private val setBudget: SetBudgetUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    private val currentMonth = DateUtils.getCurrentMonth()
    private val currentYear = DateUtils.getCurrentYear()

    init {
        loadBudgets()
        loadCategories()
    }

    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(
            showAddDialog = true,
            dialogCategoryId = -1,
            dialogAmount = ""
        )
    }

    fun hideDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }

    fun onDialogCategoryChange(categoryId: Long) {
        _uiState.value = _uiState.value.copy(dialogCategoryId = categoryId)
    }

    fun onDialogAmountChange(amount: String) {
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
            _uiState.value = _uiState.value.copy(dialogAmount = amount)
        }
    }

    fun saveBudget() {
        viewModelScope.launch {
            val state = _uiState.value
            val amount = state.dialogAmount.toDoubleOrNull() ?: return@launch
            if (state.dialogCategoryId <= 0) return@launch

            try {
                setBudget(
                    Budget(
                        categoryId = state.dialogCategoryId,
                        amount = amount,
                        month = currentMonth,
                        year = currentYear
                    )
                )
                hideDialog()
            } catch (_: Exception) { }
        }
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            getBudgetStatus(currentMonth, currentYear)
                .catch { /* handle error */ }
                .collect { budgets ->
                    _uiState.value = _uiState.value.copy(
                        budgets = budgets,
                        isLoading = false
                    )
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategoriesByType(TransactionType.EXPENSE).collect { categories ->
                _uiState.value = _uiState.value.copy(expenseCategories = categories)
            }
        }
    }
}
