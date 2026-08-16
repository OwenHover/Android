package com.example.expensetracker;

// This class represents ONE expense.
// It doesn't do any database work - it's just a container that holds
// the data for a single row (id, title, amount, category, date).
// We use objects like this so we can pass a whole expense around easily
// instead of passing 5 separate variables everywhere.
public class Expense {

    private int id;          // unique ID from the database (auto-generated)
    private String title;    // e.g. "Lunch"
    private double amount;   // e.g. 12.50
    private String category; // e.g. "Food"
    private String date;     // e.g. "01 Jul 2026"

    // Constructor: runs when we create a new Expense object
    public Expense(int id, String title, double amount, String category, String date) {
        this.id = id;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    // Getter methods so other classes can READ these values
    // (fields are private, so this is the only way to access them - encapsulation)
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }
}