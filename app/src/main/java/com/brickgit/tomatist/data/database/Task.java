package com.brickgit.tomatist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Created by Daniel Lin on 2018/10/13. */
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


  public long getTaskId() {
    return taskId;
  }

  public void setTaskId(long taskId) {
    this.taskId = taskId;
  }

  public long getProjectId() {
    return projectId;
  }

  public void setProjectId(long projectId) {
    this.projectId = projectId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean finished) {
    isFinished = finished;
  }
}
