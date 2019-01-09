package com.brickgit.tomatist.data.database;

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

  @ColumnInfo(name = "project_id")
  private long projectId;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "is_finished")
  private boolean isFinished;
}
