package com.sai.expensetracker.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sai.expensetracker.presentation.budget.BudgetScreen
import com.sai.expensetracker.presentation.dashboard.DashboardScreen
import com.sai.expensetracker.presentation.settings.SettingsScreen
import com.sai.expensetracker.presentation.statistics.StatisticsScreen
import com.sai.expensetracker.presentation.transactions.AddEditTransactionScreen
import com.sai.expensetracker.presentation.transactions.TransactionsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToTransactions = {
                    navController.navigate(Screen.Transactions.route)
                },
                onNavigateToAddTransaction = {
                    navController.navigate(Screen.AddEditTransaction.createRoute())
                }
            )
        }

        composable(Screen.Transactions.route) {
            TransactionsScreen(
                onNavigateToAddTransaction = {
                    navController.navigate(Screen.AddEditTransaction.createRoute())
                },
                onNavigateToEditTransaction = { transactionId ->
                    navController.navigate(Screen.AddEditTransaction.createRoute(transactionId))
                }
            )
        }

        composable(
            route = Screen.AddEditTransaction.route,
            arguments = listOf(
                navArgument("transactionId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            AddEditTransactionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(Screen.Statistics.route) {
            StatisticsScreen()
        }

        composable(Screen.Budget.route) {
            BudgetScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen(
                isDarkTheme = isDarkTheme,
                onToggleTheme = onToggleTheme
            )
        }
    }
}
