package edu.neu.madcourse.cs5520_sp22_final_project;

import android.graphics.Paint;
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
        Reminder item = itemList.get(position);
        holder.title.setText(item.title);
        holder.currentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (holder.itemCheck.isChecked()) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.itemCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
