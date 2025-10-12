package com.sai.expensetracker.data.mapper

import com.sai.expensetracker.data.local.entity.CategoryEntity
import com.sai.expensetracker.domain.model.Category
import com.sai.expensetracker.domain.model.TransactionType

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        icon = icon,
        color = color,
        type = TransactionType.valueOf(type)
    )
}

fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        icon = icon,
        color = color,
        type = type.name
    )
}

fun List<CategoryEntity>.toDomainList(): List<Category> = map { it.toDomain() }
