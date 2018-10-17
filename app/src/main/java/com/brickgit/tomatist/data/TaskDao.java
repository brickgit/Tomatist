package com.brickgit.tomatist.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/** Created by Daniel Lin on 2018/10/13. */
@Dao
public interface TaskDao {
  @Query("SELECT * FROM tasks WHERE parent_task_id IS 0")
  List<Task> getRootTasks();

  @Insert
  void insertTask(Task task);

  @Delete
  void deleteTask(Task task);
}
