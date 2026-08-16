package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

// RecyclerView can't display a List<Expense> directly - it needs an
// "Adapter" that knows how to turn each Expense object into a row of
// views on screen. This class is that translator.
public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    // A custom interface so this adapter can "call back" to MainActivity
    // whenever the delete button is tapped on a row. This keeps the
    // adapter from needing to know about the database directly.
    public interface OnDeleteClickListener {
        void onDeleteClick(Expense expense);
    }

    private List<Expense> expenseList;
    private final OnDeleteClickListener deleteListener;

    public ExpenseAdapter(List<Expense> expenseList, OnDeleteClickListener deleteListener) {
        this.expenseList = expenseList;
        this.deleteListener = deleteListener;
    }

    // Called from MainActivity after we add/delete something in the database,
    // so the on-screen list matches what's actually stored.
    public void updateList(List<Expense> newList) {
        this.expenseList = newList;
        notifyDataSetChanged(); // tells RecyclerView "redraw everything, data changed"
    }

    // Called when RecyclerView needs to create a NEW row (inflates the XML layout)
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    // Called every time a row needs to display data - fills in the text
    // for one specific Expense at the given "position" in the list
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.titleText.setText(expense.getTitle());
        holder.categoryText.setText(expense.getCategory());
        holder.dateText.setText(expense.getDate());
        // Format the amount as currency, e.g. 12.5 -> "$12.50"
        holder.amountText.setText(String.format(Locale.getDefault(), "$%.2f", expense.getAmount()));

        // When the trash icon on THIS row is tapped, notify MainActivity
        holder.deleteButton.setOnClickListener(v -> deleteListener.onDeleteClick(expense));
    }

    // Tells RecyclerView how many rows total to draw
    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder = holds references to the views inside ONE row so we
    // don't have to call findViewById() repeatedly (this is what makes
    // RecyclerView fast, even scrolling through hundreds of items)
    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, categoryText, dateText, amountText;
        ImageButton deleteButton;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.titleText);
            categoryText = itemView.findViewById(R.id.categoryText);
            dateText = itemView.findViewById(R.id.dateText);
            amountText = itemView.findViewById(R.id.amountText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}