package com.quantumkubernets.privatenotetakingapp;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskRepository {


    private TaskDAO noteDao;

    private LiveData<List<Task>> allNotes;

    public TaskRepository(Application application) {

        TaskDatabase noteDatabase = TaskDatabase.getInstance(application);
        noteDao = noteDatabase.noteDAO();
        allNotes = noteDao.getAllNotes();

    }

    public void insert(Task note) {
        new InsertNoteAsyncTask(noteDao).execute(note);

    }

    public void Update(Task note) {

        new UpdateNoteAsyncTask(noteDao).execute(note);

    }

    public void delete(Task note) {

        new DeleteNoteAsyncTask(noteDao).execute(note);

    }

    public void delelteAllNotes() {

        new DeleteAllNoteAsyncTask(noteDao).execute();

    }

    public LiveData<List<Task>> getAllNotes() {
        Log.d("ASHISH", allNotes.toString());
        return  allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDAO noteDao;
        private InsertNoteAsyncTask(TaskDAO noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Task... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }


    private static class UpdateNoteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDAO noteDao;
        private UpdateNoteAsyncTask(TaskDAO noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Task... notes) {
            noteDao.Update(notes[0]);
            return null;
        }
    }


    private static class DeleteNoteAsyncTask extends AsyncTask<Task, Void, Void> {
        private TaskDAO noteDao;
        private DeleteNoteAsyncTask(TaskDAO noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Task... notes) {
            noteDao.Delete(notes[0]);
            return null;
        }
    }


    private static class DeleteAllNoteAsyncTask extends AsyncTask<Void, Void, Void> {
        private TaskDAO noteDao;
        private DeleteAllNoteAsyncTask(TaskDAO noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }


}
