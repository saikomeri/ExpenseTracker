package com.sai.expensetracker.di

import com.sai.expensetracker.data.repository.BudgetRepositoryImpl
import com.sai.expensetracker.data.repository.CategoryRepositoryImpl
import com.sai.expensetracker.data.repository.TransactionRepositoryImpl
import com.sai.expensetracker.domain.repository.BudgetRepository
import com.sai.expensetracker.domain.repository.CategoryRepository
import com.sai.expensetracker.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        impl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        impl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindBudgetRepository(
        impl: BudgetRepositoryImpl
    ): BudgetRepository
}
