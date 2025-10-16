package com.sai.expensetracker.data.repository

import com.sai.expensetracker.data.local.dao.CategoryDao
import com.sai.expensetracker.data.local.dao.TransactionDao
import com.sai.expensetracker.data.mapper.toDomain
import com.sai.expensetracker.data.mapper.toDomainList
import com.sai.expensetracker.data.mapper.toEntity
import com.sai.expensetracker.domain.model.CategoryExpense
import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { it.toDomainList() }
    }

    override fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByType(type.name).map { it.toDomainList() }
    }

    override fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByDateRange(startDate, endDate).map { it.toDomainList() }
    }

    override fun getTransactionsByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Flow<List<Transaction>> {
        return transactionDao.getTransactionsByTypeAndDateRange(type.name, startDate, endDate)
            .map { it.toDomainList() }
    }

    override fun getRecentTransactions(limit: Int): Flow<List<Transaction>> {
        return transactionDao.getRecentTransactions(limit).map { it.toDomainList() }
    }

    override fun getTotalByTypeAndDateRange(type: TransactionType, startDate: Long, endDate: Long): Flow<Double> {
        return transactionDao.getTotalByTypeAndDateRange(type.name, startDate, endDate)
    }

    override fun getTotalIncome(): Flow<Double> = transactionDao.getTotalIncome()

    override fun getTotalExpense(): Flow<Double> = transactionDao.getTotalExpense()

    override fun getCategoryExpenses(startDate: Long, endDate: Long): Flow<List<CategoryExpense>> {
        return transactionDao.getCategoryExpenses(startDate, endDate).map { categoryTotals ->
            val totalExpense = categoryTotals.sumOf { it.total }
            categoryTotals.mapNotNull { ct ->
                val category = categoryDao.getCategoryById(ct.categoryId)
                category?.let {
                    CategoryExpense(
                        categoryId = ct.categoryId,
                        categoryName = it.name,
                        categoryIcon = it.icon,
                        categoryColor = it.color,
                        totalAmount = ct.total,
                        percentage = if (totalExpense > 0) (ct.total / totalExpense * 100).toFloat() else 0f
                    )
                }
            }
        }
    }

    override fun getSpentByCategory(categoryId: Long, startDate: Long, endDate: Long): Flow<Double> {
        return transactionDao.getSpentByCategory(categoryId, startDate, endDate)
    }

    override fun searchTransactions(query: String): Flow<List<Transaction>> {
        return transactionDao.searchTransactions(query).map { it.toDomainList() }
    }

    override suspend fun getTransactionById(id: Long): Transaction? {
        return transactionDao.getTransactionById(id)?.toDomain()
    }

    override suspend fun insertTransaction(transaction: Transaction): Long {
        return transactionDao.insertTransaction(transaction.toEntity())
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        transactionDao.updateTransaction(transaction.toEntity())
    }

    override suspend fun deleteTransaction(id: Long) {
        transactionDao.deleteTransactionById(id)
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }
}
