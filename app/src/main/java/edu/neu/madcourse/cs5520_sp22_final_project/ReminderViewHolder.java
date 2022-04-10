package edu.neu.madcourse.cs5520_sp22_final_project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public View currentItemView;

    public ReminderViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.reminder_title);
        currentItemView = itemView;
    }
}
