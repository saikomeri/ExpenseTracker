package com.sai.expensetracker.presentation.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.expensetracker.domain.model.CategoryExpense
import com.sai.expensetracker.domain.usecase.statistics.GetCategoryStatsUseCase
import com.sai.expensetracker.domain.usecase.statistics.GetSpendingTrendsUseCase
import com.sai.expensetracker.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatisticsUiState(
    val categoryExpenses: List<CategoryExpense> = emptyList(),
    val spendingTrends: Map<String, Double> = emptyMap(),
    val totalExpense: Double = 0.0,
    val selectedPeriod: StatsPeriod = StatsPeriod.MONTHLY,
    val isLoading: Boolean = true
)

enum class StatsPeriod { WEEKLY, MONTHLY }

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getCategoryStats: GetCategoryStatsUseCase,
    private val getSpendingTrends: GetSpendingTrendsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    fun setPeriod(period: StatsPeriod) {
        _uiState.value = _uiState.value.copy(selectedPeriod = period)
        loadStats()
    }

    private fun loadStats() {
        val (startDate, endDate) = when (_uiState.value.selectedPeriod) {
            StatsPeriod.WEEKLY -> DateUtils.getStartOfWeek() to DateUtils.getEndOfDay()
            StatsPeriod.MONTHLY -> DateUtils.getStartOfMonth() to DateUtils.getEndOfMonth()
        }

        viewModelScope.launch {
            getCategoryStats(startDate, endDate)
                .catch { /* handle error */ }
                .collect { categories ->
                    _uiState.value = _uiState.value.copy(
                        categoryExpenses = categories,
                        totalExpense = categories.sumOf { it.totalAmount },
                        isLoading = false
                    )
                }
        }

        viewModelScope.launch {
            getSpendingTrends(startDate, endDate)
                .catch { /* handle error */ }
                .collect { trends ->
                    _uiState.value = _uiState.value.copy(
                        spendingTrends = trends
                    )
                }
        }
    }
}
