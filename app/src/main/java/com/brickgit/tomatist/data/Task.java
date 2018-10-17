package com.brickgit.tomatist.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;

/** Created by Daniel Lin on 2018/10/13. */
@Data
@Entity(tableName = "tasks")
public class Task {
  @PrimaryKey(autoGenerate = true)
  private long taskId;

  @ColumnInfo(name = "parent_task_id")
  private long parentTaskId;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "is_finished")
  private boolean isFinished;
}
