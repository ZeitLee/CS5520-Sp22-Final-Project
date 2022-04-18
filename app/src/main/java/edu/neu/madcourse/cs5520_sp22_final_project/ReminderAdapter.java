package edu.neu.madcourse.cs5520_sp22_final_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.madcourse.cs5520_sp22_final_project.models.Reminder;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderViewHolder> {
    private final ArrayList<Reminder> itemList;
    private SharedPreferences.Editor edit;
    private Gson gson = new Gson();
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
        holder.id = item.id;
        holder.title.setText(item.title);
        holder.deadline.setText(item.date + "  " + item.time);
        holder.itemCheck.setChecked(item.completed);


        holder.currentItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send id to create screen.
                MainActivity.getMyInstanceActivity().jumpToReminder(item.id);
            }
        });


        if (holder.itemCheck.isChecked()) {
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.deadline.setPaintFlags(holder.deadline.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        } else {
            holder.title.setPaintFlags(holder.title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            holder.deadline.setPaintFlags(holder.deadline.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.itemCheck.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                item.completed = !item.completed;
                String json = gson.toJson(item);
                edit.putString(item.id, json);
                edit.apply();
                notifyDataSetChanged();
            }
        });
    }

    public void setEdit(SharedPreferences.Editor edit) {
        this.edit = edit;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
