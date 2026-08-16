package com.example.expensetracker;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // References to views defined in activity_main.xml
    private RecyclerView recyclerView;
    private TextView totalAmountText;
    private FloatingActionButton fabAdd;

    private ExpenseDbHelper dbHelper;   // handles all database calls
    private ExpenseAdapter adapter;     // feeds data into the RecyclerView
    private List<Expense> expenseList;  // the current list of expenses in memory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link each Java variable to its matching view in the XML by ID
        recyclerView = findViewById(R.id.recyclerView);
        totalAmountText = findViewById(R.id.totalAmountText);
        fabAdd = findViewById(R.id.fabAdd);

        dbHelper = new ExpenseDbHelper(this);

        // Load whatever was already saved from a previous session
        expenseList = dbHelper.getAllExpenses();

        // Set up the adapter, passing a lambda for what happens on delete-tap
        adapter = new ExpenseAdapter(expenseList, this::confirmDelete);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // vertical scrolling list
        recyclerView.setAdapter(adapter);

        updateTotal(); // show the correct total as soon as the app opens


        //add functions
        // Tapping the + button opens the "add expense" popup
        fabAdd.setOnClickListener(v -> showAddExpenseDialog());
    }

    // Builds and shows a popup dialog with a small form:
    // title field, amount field, and a category dropdown (Spinner)
    private void showAddExpenseDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_expense, null);

        EditText titleInput = dialogView.findViewById(R.id.titleInput);
        EditText amountInput = dialogView.findViewById(R.id.amountInput);
        Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);

        // Fill the dropdown with fixed category options
        String[] categories = {"Food", "Transport", "Bills", "Shopping", "Other"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, categories);
        categorySpinner.setAdapter(spinnerAdapter);

        new AlertDialog.Builder(this)
                .setTitle("Add Expense")
                .setView(dialogView)
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String title = titleInput.getText().toString().trim();
                    String amountStr = amountInput.getText().toString().trim();
                    String category = categorySpinner.getSelectedItem().toString();

                    // Basic validation - don't save if fields are empty
                    if (TextUtils.isEmpty(title) || TextUtils.isEmpty(amountStr)) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    double amount = Double.parseDouble(amountStr);
                    // Automatically stamp today's date on the expense
                    String today = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                            .format(new Date());

                    dbHelper.addExpense(title, amount, category, today);
                    refreshList(); // reload the list so the new expense shows up
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Shows a confirmation popup before actually deleting anything -
    // prevents accidental taps from wiping out data
    private void confirmDelete(Expense expense) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Delete \"" + expense.getTitle() + "\"?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    dbHelper.deleteExpense(expense.getId());
                    refreshList();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Re-reads everything from the database and refreshes the UI.
    // We call this after every add/delete so the screen always matches
    // what's actually stored - this is the core "sync UI with data" pattern.
    private void refreshList() {
        expenseList = dbHelper.getAllExpenses();
        adapter.updateList(expenseList);
        updateTotal();
    }

    // Loops through every expense currently in memory and adds up the total
    private void updateTotal() {
        double total = 0;
        for (Expense e : expenseList) {
            total += e.getAmount();
        }
        totalAmountText.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
    }
}