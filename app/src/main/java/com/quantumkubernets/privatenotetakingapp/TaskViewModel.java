package com.quantumkubernets.privatenotetakingapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository taskRepository;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);

        taskRepository = new TaskRepository(application);
        allTasks = taskRepository.getAllNotes();
    }


    public void insert(Task task) {
        taskRepository.insert(task);
    }

    public void update(Task task) {
        taskRepository.Update(task);
    }
    public void delete(Task task) {
        taskRepository.delete(task);
    }

    public void deleteAllTasks(Task task) {
        taskRepository.delelteAllNotes();
    }

    public LiveData<List<Task>> getAllTasks() {
        return allTasks;
    }

}
