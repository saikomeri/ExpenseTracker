package com.sai.expensetracker.presentation.transactions

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sai.expensetracker.domain.model.Category
import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.repository.CategoryRepository
import com.sai.expensetracker.domain.usecase.transaction.AddTransactionUseCase
import com.sai.expensetracker.domain.usecase.transaction.DeleteTransactionUseCase
import com.sai.expensetracker.domain.usecase.transaction.GetTransactionByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddEditTransactionState(
    val amount: String = "",
    val note: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val selectedCategoryId: Long = -1,
    val date: Long = System.currentTimeMillis(),
    val categories: List<Category> = emptyList(),
    val isEditing: Boolean = false,
    val transactionId: Long = 0,
    val isLoading: Boolean = false
)

sealed class AddEditTransactionEvent {
    data object TransactionSaved : AddEditTransactionEvent()
    data object TransactionDeleted : AddEditTransactionEvent()
    data class Error(val message: String) : AddEditTransactionEvent()
}

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val addTransaction: AddTransactionUseCase,
    private val getTransactionById: GetTransactionByIdUseCase,
    private val deleteTransaction: DeleteTransactionUseCase,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AddEditTransactionState())
    val state: StateFlow<AddEditTransactionState> = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddEditTransactionEvent>()
    val event: SharedFlow<AddEditTransactionEvent> = _event.asSharedFlow()

    init {
        val transactionId = savedStateHandle.get<Long>("transactionId") ?: -1L
        if (transactionId != -1L) {
            loadTransaction(transactionId)
        }
        loadCategories()
    }

    fun onAmountChange(amount: String) {
        // Allow only valid decimal input
        if (amount.isEmpty() || amount.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
            _state.value = _state.value.copy(amount = amount)
        }
    }

    fun onNoteChange(note: String) {
        _state.value = _state.value.copy(note = note)
    }

    fun onTypeChange(type: TransactionType) {
        _state.value = _state.value.copy(type = type, selectedCategoryId = -1)
        loadCategories()
    }

    fun onCategorySelect(categoryId: Long) {
        _state.value = _state.value.copy(selectedCategoryId = categoryId)
    }

    fun onDateChange(date: Long) {
        _state.value = _state.value.copy(date = date)
    }

    fun saveTransaction() {
        viewModelScope.launch {
            val currentState = _state.value
            try {
                val amount = currentState.amount.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    _event.emit(AddEditTransactionEvent.Error("Please enter a valid amount"))
                    return@launch
                }
                if (currentState.selectedCategoryId <= 0) {
                    _event.emit(AddEditTransactionEvent.Error("Please select a category"))
                    return@launch
                }

                val transaction = Transaction(
                    id = if (currentState.isEditing) currentState.transactionId else 0,
                    amount = amount,
                    type = currentState.type,
                    categoryId = currentState.selectedCategoryId,
                    note = currentState.note.trim(),
                    date = currentState.date
                )

                addTransaction(transaction)
                _event.emit(AddEditTransactionEvent.TransactionSaved)
            } catch (e: Exception) {
                _event.emit(AddEditTransactionEvent.Error(e.message ?: "Error saving transaction"))
            }
        }
    }

    fun onDeleteTransaction() {
        viewModelScope.launch {
            try {
                deleteTransaction(_state.value.transactionId)
                _event.emit(AddEditTransactionEvent.TransactionDeleted)
            } catch (e: Exception) {
                _event.emit(AddEditTransactionEvent.Error(e.message ?: "Error deleting transaction"))
            }
        }
    }

    private fun loadTransaction(id: Long) {
        viewModelScope.launch {
            val transaction = getTransactionById(id)
            transaction?.let {
                _state.value = _state.value.copy(
                    amount = it.amount.toString(),
                    note = it.note,
                    type = it.type,
                    selectedCategoryId = it.categoryId,
                    date = it.date,
                    isEditing = true,
                    transactionId = it.id
                )
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getCategoriesByType(_state.value.type).collect { categories ->
                _state.value = _state.value.copy(categories = categories)
            }
        }
    }
}
