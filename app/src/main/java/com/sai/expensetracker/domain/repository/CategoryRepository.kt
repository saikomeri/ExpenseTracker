package com.sai.expensetracker.domain.repository

import com.sai.expensetracker.domain.model.Category
import com.sai.expensetracker.domain.model.TransactionType
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getAllCategories(): Flow<List<Category>>
    fun getCategoriesByType(type: TransactionType): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
}
