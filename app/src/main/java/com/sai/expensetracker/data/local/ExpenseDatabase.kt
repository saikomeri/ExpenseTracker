package com.sai.expensetracker.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.sai.expensetracker.data.local.dao.BudgetDao
import com.sai.expensetracker.data.local.dao.CategoryDao
import com.sai.expensetracker.data.local.dao.TransactionDao
import com.sai.expensetracker.data.local.entity.BudgetEntity
import com.sai.expensetracker.data.local.entity.CategoryEntity
import com.sai.expensetracker.data.local.entity.TransactionEntity
import com.sai.expensetracker.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        TransactionEntity::class,
        CategoryEntity::class,
        BudgetEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        fun buildDatabase(context: Context): ExpenseDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ExpenseDatabase::class.java,
                Constants.DATABASE_NAME
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            prepopulateCategories(db)
                        }
                    }
                })
                .fallbackToDestructiveMigration()
                .build()
        }

        private fun prepopulateCategories(db: SupportSQLiteDatabase) {
            // Expense categories
            val expenseCategories = listOf(
                Triple("Food & Dining", "restaurant", 0xFFE53935L),
                Triple("Transport", "directions_car", 0xFF1E88E5L),
                Triple("Shopping", "shopping_bag", 0xFF8E24AAL),
                Triple("Bills & Utilities", "receipt_long", 0xFFFF6F00L),
                Triple("Entertainment", "movie", 0xFFD81B60L),
                Triple("Health", "favorite", 0xFF43A047L),
                Triple("Education", "school", 0xFF3949ABL),
                Triple("Groceries", "local_grocery_store", 0xFF00897BL),
                Triple("Travel", "flight", 0xFF00ACC1L),
                Triple("Other", "more_horiz", 0xFF757575L)
            )

            // Income categories
            val incomeCategories = listOf(
                Triple("Salary", "work", 0xFF2E7D32L),
                Triple("Freelance", "laptop", 0xFF1565C0L),
                Triple("Investment", "trending_up", 0xFF6A1B9AL),
                Triple("Gift", "card_giftcard", 0xFFEF6C00L),
                Triple("Other Income", "account_balance_wallet", 0xFF546E7AL)
            )

            expenseCategories.forEach { (name, icon, color) ->
                db.execSQL(
                    "INSERT INTO categories (name, icon, color, type) VALUES (?, ?, ?, ?)",
                    arrayOf(name, icon, color, "EXPENSE")
                )
            }

            incomeCategories.forEach { (name, icon, color) ->
                db.execSQL(
                    "INSERT INTO categories (name, icon, color, type) VALUES (?, ?, ?, ?)",
                    arrayOf(name, icon, color, "INCOME")
                )
            }
        }
    }
}
