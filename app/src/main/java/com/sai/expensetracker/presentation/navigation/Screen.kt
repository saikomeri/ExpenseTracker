package com.sai.expensetracker.presentation.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Transactions : Screen("transactions")
    data object AddEditTransaction : Screen("add_edit_transaction?transactionId={transactionId}") {
        fun createRoute(transactionId: Long? = null): String {
            return if (transactionId != null) {
                "add_edit_transaction?transactionId=$transactionId"
            } else {
                "add_edit_transaction"
            }
        }
    }
    data object Statistics : Screen("statistics")
    data object Budget : Screen("budget")
    data object Settings : Screen("settings")
}
