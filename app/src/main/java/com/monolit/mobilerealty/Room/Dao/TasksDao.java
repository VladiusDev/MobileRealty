package com.monolit.mobilerealty.Room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.monolit.mobilerealty.RealtorObjects.Task;

import java.util.List;

@Dao
public interface TasksDao {

    @Query("SELECT * FROM tasks ORDER BY date DESC")
    List<Task> getAllTasks();

    @Query("SELECT * FROM tasks WHERE title LIKE :title OR author LIKE :author OR date LIKE :date ORDER BY date DESC")
    List<Task> getTasksByFilter(String title, String author, String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTasks(List<Task> tasks);

}
