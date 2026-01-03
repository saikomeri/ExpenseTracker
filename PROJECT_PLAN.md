# ExpenseTracker - Personal Finance App

## Overview
A modern Android expense tracking application built with Kotlin and Jetpack Compose, showcasing Clean Architecture, offline-first design, and Material Design 3. Users can track income/expenses, set budgets per category, and view spending analytics through interactive charts.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Kotlin |
| UI Framework | Jetpack Compose + Material Design 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Database | Room |
| Async | Kotlin Coroutines + Flow |
| Navigation | Jetpack Compose Navigation + Bottom Nav |
| Charts | Vico (Compose-native charting library) |
| Testing | JUnit 5, Mockk, Compose UI Tests |
| Build | Gradle (Kotlin DSL), Version Catalog |

---

## Features

### 1. Dashboard Screen
- Total balance, monthly income, and monthly expenses summary cards
- Pie chart showing expense breakdown by category
- Recent transactions list (last 5)
- Quick "Add Transaction" FAB button

### 2. Transactions Screen
- Full list of all transactions with date grouping
- Filter by: All / Income / Expense
- Filter by date range (Daily, Weekly, Monthly)
- Swipe-to-delete with undo snackbar
- Search transactions by note/description

### 3. Add/Edit Transaction Screen
- Toggle: Income or Expense
- Amount input with currency formatting
- Category selector with icons (grid layout)
- Date picker
- Note/description field
- Save/Update/Delete actions

### 4. Categories
- Predefined categories with Material icons:
  - **Expense:** Food, Transport, Shopping, Bills, Entertainment, Health, Education, Other
  - **Income:** Salary, Freelance, Investment, Gift, Other
- Color-coded category chips

### 5. Statistics Screen
- Bar chart: daily/weekly/monthly spending trends
- Pie chart: category-wise expense distribution
- Top spending categories ranking
- Month-over-month comparison
- Date range selector

### 6. Budget Management
- Set monthly budget per category
- Progress bar showing spent vs. budget
- Visual warnings when approaching/exceeding budget (yellow at 80%, red at 100%)
- Budget overview on dashboard

### 7. Settings Screen
- Dark/Light theme toggle
- Currency selection (USD, EUR, GBP, INR)
- Export data (CSV)
- Clear all data (with confirmation dialog)
- App version info

---

## Architecture & Package Structure

```
com.sai.expensetracker/
│
├── data/
│   ├── local/
│   │   ├── ExpenseDatabase.kt          -- Room database class
│   │   ├── dao/
│   │   │   ├── TransactionDao.kt       -- CRUD operations for transactions
│   │   │   ├── CategoryDao.kt          -- Category queries
│   │   │   └── BudgetDao.kt            -- Budget CRUD
│   │   ├── entity/
│   │   │   ├── TransactionEntity.kt    -- Room entity for transactions
│   │   │   ├── CategoryEntity.kt       -- Room entity for categories
│   │   │   └── BudgetEntity.kt         -- Room entity for budgets
│   │   └── converter/
│   │       └── Converters.kt           -- Type converters (Date, enums)
│   ├── repository/
│   │   ├── TransactionRepositoryImpl.kt
│   │   ├── CategoryRepositoryImpl.kt
│   │   └── BudgetRepositoryImpl.kt
│   └── mapper/
│       ├── TransactionMapper.kt        -- Entity <-> Domain model mapping
│       ├── CategoryMapper.kt
│       └── BudgetMapper.kt
│
├── domain/
│   ├── model/
│   │   ├── Transaction.kt              -- Domain model
│   │   ├── Category.kt
│   │   ├── Budget.kt
│   │   ├── TransactionType.kt          -- Enum: INCOME, EXPENSE
│   │   └── DashboardSummary.kt         -- Aggregated dashboard data
│   ├── repository/
│   │   ├── TransactionRepository.kt    -- Interface
│   │   ├── CategoryRepository.kt
│   │   └── BudgetRepository.kt
│   └── usecase/
│       ├── transaction/
│       │   ├── AddTransactionUseCase.kt
│       │   ├── DeleteTransactionUseCase.kt
│       │   ├── GetTransactionsUseCase.kt
│       │   └── GetTransactionByIdUseCase.kt
│       ├── dashboard/
│       │   └── GetDashboardSummaryUseCase.kt
│       ├── statistics/
│       │   ├── GetCategoryStatsUseCase.kt
│       │   └── GetSpendingTrendsUseCase.kt
│       └── budget/
│           ├── SetBudgetUseCase.kt
│           └── GetBudgetStatusUseCase.kt
│
├── presentation/
│   ├── MainActivity.kt
│   ├── ExpenseTrackerApp.kt            -- Root composable with NavHost
│   ├── navigation/
│   │   ├── Screen.kt                   -- Sealed class for routes
│   │   ├── BottomNavItem.kt            -- Bottom nav items
│   │   └── NavGraph.kt                 -- Navigation graph setup
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Theme.kt                    -- M3 dynamic theming
│   │   ├── Type.kt
│   │   └── Shape.kt
│   ├── common/
│   │   ├── TransactionCard.kt          -- Reusable transaction list item
│   │   ├── CategoryChip.kt             -- Color-coded category chip
│   │   ├── AmountText.kt               -- Formatted currency text
│   │   ├── EmptyStateView.kt           -- Empty list placeholder
│   │   └── LoadingIndicator.kt
│   ├── dashboard/
│   │   ├── DashboardScreen.kt
│   │   ├── DashboardViewModel.kt
│   │   ├── components/
│   │   │   ├── SummaryCards.kt          -- Balance, income, expense cards
│   │   │   ├── ExpensePieChart.kt       -- Category breakdown chart
│   │   │   └── RecentTransactions.kt    -- Last 5 transactions
│   ├── transactions/
│   │   ├── TransactionsScreen.kt
│   │   ├── TransactionsViewModel.kt
│   │   ├── AddEditTransactionScreen.kt
│   │   ├── AddEditTransactionViewModel.kt
│   │   └── components/
│   │       ├── TransactionFilterChips.kt
│   │       ├── TransactionList.kt
│   │       └── CategorySelector.kt      -- Grid of category icons
│   ├── statistics/
│   │   ├── StatisticsScreen.kt
│   │   ├── StatisticsViewModel.kt
│   │   └── components/
│   │       ├── SpendingBarChart.kt
│   │       ├── CategoryPieChart.kt
│   │       └── TopCategoriesRanking.kt
│   ├── budget/
│   │   ├── BudgetScreen.kt
│   │   ├── BudgetViewModel.kt
│   │   └── components/
│   │       ├── BudgetProgressCard.kt
│   │       └── SetBudgetDialog.kt
│   └── settings/
│       ├── SettingsScreen.kt
│       └── SettingsViewModel.kt
│
├── di/
│   ├── AppModule.kt                    -- Database, shared prefs providers
│   ├── RepositoryModule.kt             -- Bind repository implementations
│   └── UseCaseModule.kt                -- Provide use cases
│
└── util/
    ├── CurrencyFormatter.kt
    ├── DateUtils.kt
    └── Constants.kt
```

---

## Dependencies (build.gradle.kts)

```kotlin
// Compose BOM
val composeBom = "2024.12.01"

// Core
implementation("androidx.core:core-ktx:1.15.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
implementation("androidx.activity:activity-compose:1.9.3")

// Compose
implementation(platform("androidx.compose:compose-bom:$composeBom"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Navigation
implementation("androidx.navigation:navigation-compose:2.8.5")

// Hilt
implementation("com.google.dagger:hilt-android:2.53.1")
kapt("com.google.dagger:hilt-compiler:2.53.1")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
kapt("androidx.room:room-compiler:2.6.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

// Charts - Vico
implementation("com.patrykandpatrick.vico:compose-m3:2.0.1")

// DataStore Preferences (for settings)
implementation("androidx.datastore:datastore-preferences:1.1.1")

// Testing
testImplementation("junit:junit:4.13.2")
testImplementation("io.mockk:mockk:1.13.13")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.9.0")
androidTestImplementation("androidx.compose.ui:ui-test-junit4")
```

---

## Implementation Order

### Phase 1: Project Setup
1. Create Android project with Kotlin DSL Gradle
2. Configure dependencies and version catalog
3. Set up Hilt application class
4. Create theme (Color, Typography, Theme with dark/light)
5. Set up navigation shell with bottom nav bar

### Phase 2: Data Layer
6. Define Room entities (Transaction, Category, Budget)
7. Create DAOs with Flow-returning queries
8. Build Room database with prepopulated categories
9. Implement mappers (Entity <-> Domain)
10. Implement repository classes

### Phase 3: Domain Layer
11. Define domain models
12. Define repository interfaces
13. Implement use cases (Transaction CRUD first)
14. Implement dashboard summary use case
15. Implement statistics use cases

### Phase 4: Presentation - Core Screens
16. Dashboard screen with ViewModel
17. Transaction list screen with filtering
18. Add/Edit transaction screen with form validation
19. Common composable components

### Phase 5: Presentation - Analytics & Budget
20. Statistics screen with charts (Vico)
21. Budget management screen
22. Budget progress indicators on dashboard

### Phase 6: Polish & Settings
23. Settings screen (theme toggle, currency)
24. Empty states and loading indicators
25. Swipe-to-delete with undo
26. CSV export functionality
27. UI polish and animations

### Phase 7: Testing
28. Unit tests for use cases
29. Unit tests for ViewModels
30. Compose UI tests for key screens

---

## Database Schema

### transactions
| Column | Type | Description |
|--------|------|-------------|
| id | Long (PK, auto) | Unique ID |
| amount | Double | Transaction amount |
| type | String | "INCOME" or "EXPENSE" |
| categoryId | Long (FK) | Category reference |
| note | String? | Optional description |
| date | Long | Timestamp |
| createdAt | Long | Record creation time |

### categories
| Column | Type | Description |
|--------|------|-------------|
| id | Long (PK, auto) | Unique ID |
| name | String | Category name |
| icon | String | Material icon name |
| color | Long | Color as ARGB long |
| type | String | "INCOME" or "EXPENSE" |

### budgets
| Column | Type | Description |
|--------|------|-------------|
| id | Long (PK, auto) | Unique ID |
| categoryId | Long (FK) | Category reference |
| amount | Double | Budget limit |
| month | Int | Month (1-12) |
| year | Int | Year |
