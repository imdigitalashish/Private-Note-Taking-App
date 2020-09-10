package com.quantumkubernets.privatenotetakingapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import java.time.Year;
import java.util.Calendar;

public class AddOrEditActivity extends AppCompatActivity {

    public static final String TAG = "AddOrEditActivity";

    public static final String EXTRA_ID =
            "com.quantumkubernets.privatenotetakingapp.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.quantumkubernets.privatenotetakingapp.EXTRA_TITLE";
    public static final String EXTRA_PRIORITY =
            "com.quantumkubernets.privatenotetakingapp.EXTRA_PRIORITY";
    public static final String EXTRA_DUE_DATE =
            "com.quantumkubernets.privatenotetakingapp.EXTRA_DUE_DATE";
    public static final String EXTRA_DATE =
            "com.quantumkubernets.privatenotetakingapp.EXTRA_DATE";
    public static final String EXTRA_MONTH =
            "com.quantumkubernets.privatenotetakingapp.EXTRA_MONTH";
    public static final String EXTRA_YEAR =
            "com.quantumkubernets.privatenotetakingapp.EXTRA_YEAR";

    DatePicker datePicker;
    Button btnSave;
    EditText et_task_name;
    RadioGroup priority, dueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit);
        datePicker = findViewById(R.id.datePicker);
        btnSave = findViewById(R.id.btnSave);

        et_task_name = findViewById(R.id.your_task);

        priority = findViewById(R.id.priority_group);
        dueDate = findViewById(R.id.due_date_mode);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {

//            et_task_name.setText(intent.getIntExtra(EXTRA_TITLE));
            et_task_name.setText(String.valueOf(intent.getIntExtra(EXTRA_ID, 34)));
            RadioButton radioButtonPriority = findViewById(Integer.valueOf(intent.getStringExtra(EXTRA_PRIORITY)));
            RadioButton radioButtonDueDate = findViewById(Integer.valueOf(intent.getStringExtra(EXTRA_DUE_DATE)));
            radioButtonPriority.setChecked(true);
            radioButtonDueDate.setChecked(true);
            datePicker.init(intent.getIntExtra(EXTRA_YEAR, 1), intent.getIntExtra(EXTRA_MONTH, 1), intent.getIntExtra(EXTRA_DATE, 1), null);
            btnSave.setText("Update Task");
        }

    }


    public void addMyTask(View view) {

        // TODO set Time to Due Date

        Calendar c = Calendar.getInstance();

        c.set(Calendar.YEAR, datePicker.getYear());
        c.set(Calendar.MONTH, datePicker.getMonth());
        c.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        c.set(Calendar.HOUR_OF_DAY, 22);
        c.set(Calendar.MINUTE, 16);
        c.set(Calendar.SECOND, 0);


        String title = et_task_name.getText().toString();
        String priority_id = String.valueOf(priority.getCheckedRadioButtonId());
        String dueDate_id = String.valueOf(dueDate.getCheckedRadioButtonId());
        int date = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Intent intent = new Intent(AddOrEditActivity.this, Home.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_PRIORITY, priority_id);
        intent.putExtra(EXTRA_DUE_DATE, dueDate_id);
        intent.putExtra(EXTRA_DATE, date);
        intent.putExtra(EXTRA_MONTH, month);
        intent.putExtra(EXTRA_YEAR, year);


        setAlarm(c);
        Log.d(TAG, "Alarm Setted");
        setResult(RESULT_OK, intent);
        finish();


    }

    private void setAlarm(Calendar c) {

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(AddOrEditActivity.this, MyIntentService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, intent.getIntExtra("NOTIFICATION_ID", 1), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        Log.d(TAG, "Notification Set For ID : " + intent.getIntExtra(EXTRA_ID, 1));
    }
}

// TODO Remove this annotationProcessor
//The following annotation processors are not incremental: lifecycle-compiler-2.0.0.jar (androidx.lifecycle:lifecycle-compiler:2.0.0), room-compiler-2.0.0.jar (androidx.room:room-compiler:2.0.0).