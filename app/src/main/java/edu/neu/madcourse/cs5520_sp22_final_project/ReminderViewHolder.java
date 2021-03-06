package edu.neu.madcourse.cs5520_sp22_final_project;

import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView deadline;

    public View currentItemView;
    public CheckBox itemCheck;
    public String id;

    public ReminderViewHolder(@NonNull View itemView) {
        super(itemView);
        deadline = itemView.findViewById(R.id.reminder_deadline);
        title = itemView.findViewById(R.id.reminder_title);
        itemCheck = itemView.findViewById(R.id.task_checkbox);
        currentItemView = itemView;
    }
}
