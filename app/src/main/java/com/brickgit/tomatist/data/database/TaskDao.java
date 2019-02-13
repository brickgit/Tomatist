package com.brickgit.tomatist.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/** Created by Daniel Lin on 2018/10/13. */
@Dao
public interface TaskDao {
  @Query("SELECT * FROM tasks")
  LiveData<List<Task>> getTasks();

  @Insert
  void insertTask(Task task);

  @Delete
  void deleteTask(Task task);
}
