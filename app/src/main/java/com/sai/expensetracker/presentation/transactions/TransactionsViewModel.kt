package com.sai.expensetracker.presentation.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.usecase.transaction.DeleteTransactionUseCase
import com.sai.expensetracker.domain.usecase.transaction.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val filter: TransactionFilter = TransactionFilter.ALL,
    val isLoading: Boolean = true,
    val error: String? = null
)

enum class TransactionFilter {
    ALL, INCOME, EXPENSE
}

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactions: GetTransactionsUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    fun setFilter(filter: TransactionFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
        loadTransactions()
    }

    fun deleteTransaction(id: Long) {
        viewModelScope.launch {
            deleteTransactionUseCase(id)
        }
    }

    private fun loadTransactions() {
        val type = when (_uiState.value.filter) {
            TransactionFilter.ALL -> null
            TransactionFilter.INCOME -> TransactionType.INCOME
            TransactionFilter.EXPENSE -> TransactionType.EXPENSE
        }

        viewModelScope.launch {
            getTransactions(type = type)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
                .collect { transactions ->
                    _uiState.value = _uiState.value.copy(
                        transactions = transactions,
                        isLoading = false
                    )
                }
        }
    }
}
