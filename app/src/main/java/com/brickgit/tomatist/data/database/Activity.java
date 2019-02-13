package com.brickgit.tomatist.data.database;

import java.util.Date;

import javax.annotation.Nullable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "activities",
    indices = {@Index("task_id"), @Index("category_id")},
    foreignKeys = {
      @ForeignKey(
          entity = Task.class,
          parentColumns = "id",
          childColumns = "task_id",
          onDelete = ForeignKey.SET_NULL),
      @ForeignKey(
          entity = Category.class,
          parentColumns = "id",
          childColumns = "category_id",
          onDelete = ForeignKey.SET_NULL)
    })
public class Activity {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private Long activityId;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "start_time")
  private Date startTime;

  @ColumnInfo(name = "end_time")
  private Date endTime;

  @ColumnInfo(name = "minutes")
  private long minutes;

  @ColumnInfo(name = "task_id")
  @Nullable
  private Long taskId;

  @ColumnInfo(name = "note")
  private String note;

  @ColumnInfo(name = "category_id")
  @Nullable
  private Long categoryId;

  public Long getActivityId() {
    return activityId;
  }

  public void setActivityId(Long activityId) {
    this.activityId = activityId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  public long getMinutes() {
    return minutes;
  }

  public void setMinutes(long minutes) {
    this.minutes = minutes;
  }

  @Nullable
  public Long getTaskId() {
    return taskId;
  }

  public void setTaskId(@Nullable Long taskId) {
    this.taskId = taskId;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Activity)) return false;
    return this.activityId == ((Activity) obj).activityId;
  }
}
