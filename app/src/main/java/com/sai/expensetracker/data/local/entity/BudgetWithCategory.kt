package com.sai.expensetracker.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BudgetWithCategory(
    @Embedded val budget: BudgetEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)
