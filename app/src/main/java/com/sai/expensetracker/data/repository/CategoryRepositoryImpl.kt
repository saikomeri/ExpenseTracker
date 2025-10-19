package com.sai.expensetracker.data.repository

import com.sai.expensetracker.data.local.dao.CategoryDao
import com.sai.expensetracker.data.mapper.toDomain
import com.sai.expensetracker.data.mapper.toDomainList
import com.sai.expensetracker.domain.model.Category
import com.sai.expensetracker.domain.model.TransactionType
import com.sai.expensetracker.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { it.toDomainList() }
    }

    override fun getCategoriesByType(type: TransactionType): Flow<List<Category>> {
        return categoryDao.getCategoriesByType(type.name).map { it.toDomainList() }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }
}
