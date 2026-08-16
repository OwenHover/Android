package com.example.expensetracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// SQLiteOpenHelper is a built-in Android class that manages creating
// and upgrading the database for us. We just extend it and fill in
// onCreate() and onUpgrade(). No external library needed.
public class ExpenseDbHelper extends SQLiteOpenHelper {

    // Database file name and version number.
    // If we ever change the table structure later, we bump DATABASE_VERSION
    // and Android will call onUpgrade() automatically.
    private static final String DATABASE_NAME = "expenses.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names - keeping these as constants avoids
    // typos when we use them in multiple SQL statements below
    private static final String TABLE_EXPENSES = "expenses";
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_CATEGORY = "category";
    private static final String COL_DATE = "date";

    // Constructor: passes info up to the parent SQLiteOpenHelper class
    public ExpenseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // onCreate() runs ONE TIME ONLY - the very first time the app
    // creates the database file. This is where we set up our table.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + // auto-increasing unique ID
                COL_TITLE + " TEXT NOT NULL, " +
                COL_AMOUNT + " REAL NOT NULL, " +
                COL_CATEGORY + " TEXT, " +
                COL_DATE + " TEXT" +
                ")";
        db.execSQL(createTable); // actually runs the SQL command
    }

    // onUpgrade() runs if DATABASE_VERSION increases (e.g. we add a
    // new column later). For a student project, the simplest approach
    // is to just wipe and recreate the table.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(db);
    }

    // ---------- CREATE (INSERT) ----------
    // ContentValues is like a HashMap made specifically for database rows:
    // it maps column names to the values we want to insert.
    public long addExpense(String title, double amount, String category, String date) {
        SQLiteDatabase db = this.getWritableDatabase(); // open DB for writing

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, title);
        values.put(COL_AMOUNT, amount);
        values.put(COL_CATEGORY, category);
        values.put(COL_DATE, date);

        // insert() returns the new row's ID, or -1 if it failed
        long newId = db.insert(TABLE_EXPENSES, null, values);
        db.close(); // always close the connection when done
        return newId;
    }

    // ---------- READ (SELECT) ----------
    // Returns every expense in the table, newest first.
    public List<Expense> getAllExpenses() {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // ORDER BY id DESC = newest expenses appear at the top of the list
        String query = "SELECT * FROM " + TABLE_EXPENSES + " ORDER BY " + COL_ID + " DESC";
        Cursor cursor = db.rawQuery(query, null); // Cursor = pointer that moves row by row

        // moveToFirst() moves the cursor to row 0 and returns false if the table is empty
        if (cursor.moveToFirst()) {
            do {
                // Read each column out of the current row by name
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COL_DATE));

                // Build an Expense object and add it to our list
                expenseList.add(new Expense(id, title, amount, category, date));
            } while (cursor.moveToNext()); // moves to the next row until there are none left
        }

        cursor.close(); // free up memory
        db.close();
        return expenseList;
    }

    // ---------- DELETE ----------
    // Deletes the row whose id matches the one passed in.
    // We use "?" as a placeholder instead of pasting the id directly into
    // the SQL string - this protects against SQL injection.
    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}