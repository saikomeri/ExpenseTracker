package com.sai.expensetracker.domain.repository

import com.sai.expensetracker.domain.model.CategoryExpense
import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>>
    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getTransactionsByTypeAndDateRange(type: TransactionType, startDate: Long, endDate: Long): Flow<List<Transaction>>
    fun getRecentTransactions(limit: Int = 5): Flow<List<Transaction>>
    fun getTotalByTypeAndDateRange(type: TransactionType, startDate: Long, endDate: Long): Flow<Double>
    fun getTotalIncome(): Flow<Double>
    fun getTotalExpense(): Flow<Double>
    fun getCategoryExpenses(startDate: Long, endDate: Long): Flow<List<CategoryExpense>>
    fun getSpentByCategory(categoryId: Long, startDate: Long, endDate: Long): Flow<Double>
    fun searchTransactions(query: String): Flow<List<Transaction>>
    suspend fun getTransactionById(id: Long): Transaction?
    suspend fun insertTransaction(transaction: Transaction): Long
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(id: Long)
    suspend fun deleteAllTransactions()
}
