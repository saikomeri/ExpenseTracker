# ExpenseTracker

A modern Android expense tracking application built with Kotlin and Jetpack Compose, following Clean Architecture principles with MVVM pattern.

## Features

- **Dashboard** - Overview of total balance, monthly income/expenses with animated pie chart
- **Transaction Management** - Add, edit, and delete income/expense transactions
- **Category System** - 15 predefined categories with Material icons and color coding
- **Statistics** - Animated donut chart, category rankings with progress bars
- **Budget Tracking** - Set monthly budgets per category with visual progress indicators
- **Dark/Light Theme** - Full Material Design 3 theming support
- **Offline-First** - All data stored locally with Room database

## Tech Stack

| Technology | Purpose |
|-----------|---------|
| **Kotlin** | Primary language |
| **Jetpack Compose** | Declarative UI with Material Design 3 |
| **MVVM + Clean Architecture** | Separation of concerns with data/domain/presentation layers |
| **Hilt** | Dependency Injection |
| **Room** | Local database with Flow-based reactive queries |
| **Kotlin Coroutines + Flow** | Asynchronous data streams |
| **Jetpack Navigation** | Type-safe Compose navigation with bottom nav |
| **DataStore** | Preferences storage |

## Architecture

```
com.sai.expensetracker/
├── data/           # Room entities, DAOs, repository implementations
├── domain/         # Business models, repository interfaces, use cases
├── presentation/   # Compose screens, ViewModels, theme, navigation
├── di/             # Hilt dependency injection modules
└── util/           # Utility classes (currency formatting, date helpers)
```

## Screenshots

| Dashboard | Transactions | Statistics | Budget |
|-----------|-------------|------------|--------|
| Summary cards, pie chart, recent transactions | Filtered list with date grouping | Donut chart with category rankings | Progress bars with budget limits |

## Setup

1. Clone the repository
2. Open in Android Studio (Hedgehog or newer)
3. Sync Gradle and run on emulator/device (API 26+)

## Author

**Sai Sampurna Komeri** - Android Mobile Developer
