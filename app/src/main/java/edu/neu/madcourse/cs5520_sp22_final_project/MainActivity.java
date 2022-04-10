package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.madcourse.cs5520_sp22_final_project.models.Reminder;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReminderAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;

    private final ArrayList<Reminder> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadReminders();
        createRecyclerView();
    }

    private void loadReminders() {

        //load from local storage

    }

    private void createRecyclerView() {
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.reminder_recyclerView);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new ReminderAdapter(itemList);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private void createNewReminder() {


    }
}