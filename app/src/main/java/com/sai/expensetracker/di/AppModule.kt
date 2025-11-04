package com.sai.expensetracker.di

import android.content.Context
import com.sai.expensetracker.data.local.ExpenseDatabase
import com.sai.expensetracker.data.local.dao.BudgetDao
import com.sai.expensetracker.data.local.dao.CategoryDao
import com.sai.expensetracker.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return ExpenseDatabase.buildDatabase(context)
    }

    @Provides
    fun provideTransactionDao(database: ExpenseDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideCategoryDao(database: ExpenseDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideBudgetDao(database: ExpenseDatabase): BudgetDao {
        return database.budgetDao()
    }
}
