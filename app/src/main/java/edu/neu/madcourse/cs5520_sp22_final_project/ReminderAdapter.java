package edu.neu.madcourse.cs5520_sp22_final_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.madcourse.cs5520_sp22_final_project.models.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {
    private final ArrayList<Reminder> itemList;
    public ReminderAdapter(ArrayList<Reminder> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_list_item, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
