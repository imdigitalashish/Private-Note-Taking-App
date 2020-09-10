package com.quantumkubernets.privatenotetakingapp;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TaskDAO {

    @Insert
    void insert(Task note);

    @Update
    void Update(Task note);

    @Delete
    void Delete(Task note);

    @Query("DELETE FROM task_table")
    void deleteAllNotes();

    @Query("SELECT * FROM task_table")
    LiveData<List<Task>> getAllNotes();

}
