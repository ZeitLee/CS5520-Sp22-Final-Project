package edu.neu.madcourse.cs5520_sp22_final_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import edu.neu.madcourse.cs5520_sp22_final_project.Alarm.Receiver;
import edu.neu.madcourse.cs5520_sp22_final_project.Location.Loc;
import edu.neu.madcourse.cs5520_sp22_final_project.models.Reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReminderAdapter rviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;
    private TextView dateDisplay;
    private TextView scoreDisplay;
    private ImageView addIcon;
    private ArrayList<String> idList;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor edit;
    private Gson gson;
    private Integer score;

    //location
    private Loc loc;

    public static WeakReference<MainActivity> weakActivity;

    //allList
    private final List<Reminder> allList = new ArrayList<>();
    private final ArrayList<Reminder> itemList = new ArrayList<>();

    //hashTag search
    private Spinner hashTag;
    private ImageView searchBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weakActivity = new WeakReference<>(MainActivity.this);
        setContentView(R.layout.activity_main);
        loc = new Loc(this);
        hashTag = (Spinner) findViewById(R.id.search);
        searchBtn = (ImageView) findViewById(R.id.searchBtn);
        loadComponent();
        loadListener();
        loadReminders();

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

                        //Cancel alarm
                        int Alarm_No = itemList.get(position).Alarm_No;
                        System.out.println("Alarm_no in Swipe" + Alarm_No);
                        Intent intent = new Intent(MainActivity.this, Receiver.class);
                        PendingIntent pItent = PendingIntent.getBroadcast(MainActivity.this, Alarm_No, intent, PendingIntent.FLAG_MUTABLE);
                        AlarmManager alarm = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
                        alarm.cancel(pItent);

                        edit.remove(id).commit();
                        idList.remove(id);
                        itemList.remove(position);
                        rviewAdapter.notifyItemRemoved(position);
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loc = new Loc(this);
                }
        }
    }

    private void searchHashTag(int item, ArrayList list) {
        list.clear();
        for(Reminder reminder : allList) {
            if (item == 0) list.add(reminder);
            else if (reminder.hashtag == item - 1) list.add(reminder);
        }
    }

    private void loadComponent() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, MMM d");
        String currentDate = dateFormat.format(calendar.getTime());
        dateDisplay = findViewById(R.id.main_date);
        dateDisplay.setText(currentDate);
        addIcon = findViewById(R.id.add_icon);
        scoreDisplay = findViewById(R.id.score);
        mSharedPreferences = this.getSharedPreferences("reminder_info", MODE_PRIVATE);
        edit = mSharedPreferences.edit();
        gson = new Gson();
        score = Integer.parseInt(mSharedPreferences.getString("score", "0"));
        scoreDisplay.setText("Scores: " + score);
        createRecyclerView();
    }

    private void loadListener() {
        addIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewReminder();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked");
                int item = hashTag.getSelectedItemPosition();
                System.out.println("item number: " + item);
                searchHashTag(item, itemList);
                System.out.println(itemList);
                rviewAdapter.notifyDataSetChanged();
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
                allList.add(obj);
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
        rviewAdapter.setScore(scoreDisplay, score);
        recyclerView.setAdapter(rviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    private void createNewReminder() {
        Reminder newItem = new Reminder();
        allList.add(0, newItem);
        searchHashTag(0, itemList);
        rviewAdapter.notifyItemInserted(0);
        idList.add(newItem.id);
        saveIdList();
        saveItem(newItem);
        //naviagte to create page.
        String id = newItem.id;
        jumpToReminder(id);
    }

    public void jumpToReminder(String id) {
        Intent i = new Intent(this, createReminder.class);
        i.putExtra("id", id);
        i.putExtra("loc", loc.getGeoLoc());
        startActivity(i);
    }

    // get instance of main.
    public static MainActivity getMyInstanceActivity() {
        return weakActivity.get();
    }

}