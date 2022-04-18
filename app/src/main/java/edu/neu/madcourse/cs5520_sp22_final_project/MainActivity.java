package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.madcourse.cs5520_sp22_final_project.models.Reminder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReminderAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private TextView dateDisplay;
    private ImageView addIcon;
    private ArrayList<String> idList;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor edit;
    private Gson gson;

    public static WeakReference<MainActivity> weakActivity;

    private final ArrayList<Reminder> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadComponent();
        loadListener();
        loadReminders();


        //TODO: delete this part after implementing click items in the list.
        //***********************  for testing load.
        EditText sk = findViewById(R.id.storageKey);
        sk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mockButton(sk.getText().toString());
            }
        });

        // *********** ****************

        weakActivity = new WeakReference<>(MainActivity.this);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        Collections.swap(itemList, fromPos, toPos);
                        rviewAdapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }
                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getLayoutPosition();
                        String id = itemList.get(position).id;
                        edit.remove(id).commit();
                        idList.remove(id);
                        itemList.remove(position);
                        rviewAdapter.notifyItemRemoved(position);
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void loadComponent() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
        String currentDate = dateFormat.format(calendar.getTime());
        dateDisplay = findViewById(R.id.main_date);
        dateDisplay.setText(currentDate);
        addIcon = findViewById(R.id.add_icon);
        mSharedPreferences = this.getSharedPreferences("reminder_info", MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        gson = new Gson();
        createRecyclerView();
    }

    private void loadListener() {
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newReminder(view);
            }
        });

    }

    private ArrayList<String> getIdList() {
        String json = mSharedPreferences.getString("idList", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> res = gson.fromJson(json, type);
        return res != null ? res : new ArrayList<String>();
    }

    private void saveIdList() {
        String json = gson.toJson(idList);
        edit.putString("idList", json);
        edit.apply();
    }

    private void saveItem(Reminder item) {
        String json = gson.toJson(item);
        edit.putString(item.id, json);
        edit.apply();
    }

    private void loadReminders() {
        idList = getIdList();
        for (String id : idList) {
            String json = mSharedPreferences.getString(id, "");
            Reminder obj = gson.fromJson(json, Reminder.class);
            if (obj != null) {
                itemList.add(obj);
            }
        }
    }

    private void createRecyclerView() {
        rLayoutManger = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.reminder_recyclerView);
        recyclerView.setHasFixedSize(true);
        rviewAdapter = new ReminderAdapter(itemList);
        rviewAdapter.setEdit(edit);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private void createNewReminder() {
        Reminder newItem = new Reminder();
        itemList.add(0, newItem);
        rviewAdapter.notifyItemInserted(0);
        idList.add(newItem.id);
        saveIdList();
        saveItem(newItem);
        //naviagte to create page
    }

    private void newReminder(View V){
        //view occupies a rectangular area on the screen and
        // is responsible for drawing and event handling
        //Toast.makeText(getApplicationContext(), "wiggle wiggle", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, createReminder.class);
        startActivity(intent);
    }

    // get instance of main.
    public static MainActivity getMyInstanceActivity() {
        return weakActivity.get();
    }

    // Create a task in to list based on the given value.
    public void createNewReminder(String json) {
        Reminder newItem = gson.fromJson(json, Reminder.class);
        itemList.add(0, newItem);
        rviewAdapter.notifyItemInserted(0);
        idList.add(newItem.id);
        saveIdList();
        saveItem(newItem);
    }

    // mock button for testing load data.
    private void mockButton(String key) {
        if (!mSharedPreferences.contains(key)) {
            Intent i = new Intent(this, createReminder.class);
            startActivity(i);
            return;
        }
        String data = mSharedPreferences.getString(key, null);
        Intent i = new Intent(this, createReminder.class);
        i.putExtra(key, data);
        i.putExtra("keyName", key);
        startActivity(i);
    }

}