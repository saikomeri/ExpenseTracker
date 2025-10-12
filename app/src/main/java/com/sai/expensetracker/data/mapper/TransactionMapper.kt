package com.sai.expensetracker.data.mapper

import com.sai.expensetracker.data.local.entity.TransactionEntity
import com.sai.expensetracker.data.local.entity.TransactionWithCategory
import com.sai.expensetracker.domain.model.Transaction
import com.sai.expensetracker.domain.model.TransactionType

fun TransactionWithCategory.toDomain(): Transaction {
    return Transaction(
        id = transaction.id,
        amount = transaction.amount,
        type = TransactionType.valueOf(transaction.type),
        categoryId = transaction.categoryId,
        categoryName = category.name,
        categoryIcon = category.icon,
        categoryColor = category.color,
        note = transaction.note,
        date = transaction.date,
        createdAt = transaction.createdAt
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        type = type.name,
        categoryId = categoryId,
        note = note,
        date = date,
        createdAt = createdAt
    )
}

fun List<TransactionWithCategory>.toDomainList(): List<Transaction> = map { it.toDomain() }
