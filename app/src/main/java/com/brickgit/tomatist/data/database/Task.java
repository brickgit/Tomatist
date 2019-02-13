package com.brickgit.tomatist.data.database;

import javax.annotation.Nullable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/** Created by Daniel Lin on 2018/10/13. */
@Entity(
    tableName = "tasks",
    indices = {@Index("category_id")},
    foreignKeys =
        @ForeignKey(
            entity = Category.class,
            parentColumns = "id",
            childColumns = "category_id",
            onDelete = ForeignKey.SET_NULL))
public class Task {
  @PrimaryKey(autoGenerate = true)
  private long taskId;

  @ColumnInfo(name = "category_id")
  @Nullable
  private Long categoryId;

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

  @Nullable
  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(@Nullable Long categoryId) {
    this.categoryId = categoryId;
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
