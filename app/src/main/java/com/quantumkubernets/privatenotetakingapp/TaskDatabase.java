package com.quantumkubernets.privatenotetakingapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Task.class, version = 4)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDAO noteDAO();

    public static synchronized TaskDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskDatabase.class, "task_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }

        return instance;

    }

    public static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
//            new PopulateDbAsynTask(instance).execute();
        }
    };

//     This below is checked to use if we data is adding to our entity

//    private static class PopulateDbAsynTask extends AsyncTask<Void, Void, Void> {
//
//        private TaskDAO noteDao;
//        private PopulateDbAsynTask(TaskDatabase db) {
//            noteDao = db.noteDAO();
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            noteDao.insert(new Task("Title 1", "Description 1", "1", 5,6,7));
//            noteDao.insert(new Task("Title 2", "Description 2", "2",5,6,5));
//            noteDao.insert(new Task("Title 3", "Description 3", "3",6,7,8));
//            return null;
//        }
//    }
//

}