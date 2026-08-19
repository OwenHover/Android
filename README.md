# Expense Tracker (Android)

An Android app for tracking personal expenses, built in Java with a local SQLite database and a RecyclerView-based list UI.

## Features

- Add new expenses (title, amount, category, date)
- View all expenses in a scrollable list, newest first
- Delete expenses
- Local persistent storage using SQLite (no internet/account required)

## Tech Stack

- **Language:** Java
- **Database:** SQLite (via `SQLiteOpenHelper`)
- **UI:** RecyclerView, Android layout XML

## Project Structure

```
app/src/main/java/com/example/expensetracker/
├── MainActivity.java       # Main screen, handles UI and user interaction
├── Expense.java            # Data model for a single expense
├── ExpenseDbHelper.java     # SQLite database helper (create, read, delete operations)
└── ExpenseAdapter.java      # RecyclerView adapter for displaying the expense list

app/src/main/res/layout/
├── activity_main.xml        # Main screen layout
├── item_expense.xml         # Layout for a single expense row
└── dialog_add_expense.xml   # Dialog for adding a new expense
```

## Getting Started

### Prerequisites

- Android Studio
- Android SDK (min SDK as configured in `app/build.gradle.kts`)

### Running the App

1. Clone the repository
   ```bash
   git clone https://github.com/your-username/expense-tracker-android.git
   ```
2. Open the project in Android Studio
3. Let Gradle sync
4. Run on an emulator or physical device

## Notes

This is a learning project focused on core Android concepts: `SQLiteOpenHelper` for local data persistence, `RecyclerView` for dynamic lists, and basic dialog-based input. Editing existing expenses is not yet implemented — planned as a future improvement.
