package com.quantumkubernets.privatenotetakingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.quantumkubernets.privatenotetakingapp.AddOrEditActivity.EXTRA_DATE;
import static com.quantumkubernets.privatenotetakingapp.AddOrEditActivity.EXTRA_DUE_DATE;
import static com.quantumkubernets.privatenotetakingapp.AddOrEditActivity.EXTRA_ID;
import static com.quantumkubernets.privatenotetakingapp.AddOrEditActivity.EXTRA_MONTH;
import static com.quantumkubernets.privatenotetakingapp.AddOrEditActivity.EXTRA_PRIORITY;
import static com.quantumkubernets.privatenotetakingapp.AddOrEditActivity.EXTRA_TITLE;
import static com.quantumkubernets.privatenotetakingapp.AddOrEditActivity.EXTRA_YEAR;

public class Home extends AppCompatActivity {

    private static final String TAG = "Home";

    private TaskViewModel taskViewModel;

    private FloatingActionButton floatingActionButton;

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    int notification_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        floatingActionButton = findViewById(R.id.addbtn);
        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);




        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                try {
                    Log.d("ASHISH", tasks.toString());
                    Task task =  tasks.get(tasks.size() - 1);
                    notification_id = task.getId();
                    Log.d(TAG, "Next Notification ID Will Be" + notification_id);
                    Log.d("ASHISH", notification_id  + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                adapter.setTasks(tasks);


            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this, AddOrEditActivity.class);
                intent.putExtra("NOTIFICATION_ID", notification_id);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Log.d(TAG, String.valueOf(adapter.getTaskAt(viewHolder.getAdapterPosition()).getId()));
                AlarmManager manager = (AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(Home.this, MyIntentService.class);
                PendingIntent pendingIntent = PendingIntent.getService(Home.this, adapter.getTaskAt(viewHolder.getAdapterPosition()).getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                manager.cancel(pendingIntent);
                Log.d(TAG, "Notification Cancelled For : " + adapter.getTaskAt(viewHolder.getAdapterPosition()).getId());

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent intent = new Intent(Home.this, AddOrEditActivity.class);
                intent.putExtra(EXTRA_ID, task.getId());
                intent.putExtra(EXTRA_TITLE, task.getTitle());
                intent.putExtra(EXTRA_PRIORITY, task.getPriority());
                intent.putExtra(EXTRA_DUE_DATE, task.getDueDate());
                intent.putExtra(EXTRA_DATE, task.getDate());
                intent.putExtra(EXTRA_MONTH, task.getMonth());
                intent.putExtra(EXTRA_YEAR, task.getYear());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddOrEditActivity.EXTRA_TITLE);
            String priority = data.getStringExtra(AddOrEditActivity.EXTRA_PRIORITY);
            String dueDate_id = data.getStringExtra(AddOrEditActivity.EXTRA_DUE_DATE);
            int date = data.getIntExtra(AddOrEditActivity.EXTRA_DATE, 1);
            int month = data.getIntExtra(AddOrEditActivity.EXTRA_MONTH, 1);
            int year = data.getIntExtra(AddOrEditActivity.EXTRA_YEAR, 1);
            Task task = new Task(title, priority, dueDate_id, date, month, year);
            taskViewModel.insert(task);
            Toast.makeText(this, "Task Added", Toast.LENGTH_SHORT).show();
        }
    }
}