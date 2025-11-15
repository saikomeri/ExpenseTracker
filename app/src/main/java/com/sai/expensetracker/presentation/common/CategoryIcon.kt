package com.sai.expensetracker.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Laptop
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

fun getCategoryIcon(iconName: String): ImageVector {
    return when (iconName) {
        "restaurant" -> Icons.Filled.Restaurant
        "directions_car" -> Icons.Filled.DirectionsCar
        "shopping_bag" -> Icons.Filled.ShoppingBag
        "receipt_long" -> Icons.Filled.ReceiptLong
        "movie" -> Icons.Filled.Movie
        "favorite" -> Icons.Filled.Favorite
        "school" -> Icons.Filled.School
        "local_grocery_store" -> Icons.Filled.LocalGroceryStore
        "flight" -> Icons.Filled.Flight
        "more_horiz" -> Icons.Filled.MoreHoriz
        "work" -> Icons.Filled.Work
        "laptop" -> Icons.Filled.Laptop
        "trending_up" -> Icons.Filled.TrendingUp
        "card_giftcard" -> Icons.Filled.CardGiftcard
        "account_balance_wallet" -> Icons.Filled.AccountBalanceWallet
        else -> Icons.Filled.MoreHoriz
    }
}
