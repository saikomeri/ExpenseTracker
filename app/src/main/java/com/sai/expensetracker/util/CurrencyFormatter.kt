package com.sai.expensetracker.util

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

object CurrencyFormatter {

    fun format(amount: Double, currencyCode: String = "USD"): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.US).apply {
            currency = Currency.getInstance(currencyCode)
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        return formatter.format(amount)
    }

    fun formatCompact(amount: Double, currencyCode: String = "USD"): String {
        val symbol = Currency.getInstance(currencyCode).symbol
        return when {
            amount >= 1_000_000 -> "${symbol}%.1fM".format(amount / 1_000_000)
            amount >= 1_000 -> "${symbol}%.1fK".format(amount / 1_000)
            else -> format(amount, currencyCode)
        }
    }
}
