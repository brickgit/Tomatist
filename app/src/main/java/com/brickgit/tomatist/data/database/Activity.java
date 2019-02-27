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
    indices = {@Index("category_id")},
    foreignKeys = {
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

  @ColumnInfo(name = "is_finished")
  private boolean isFinished;

  @ColumnInfo(name = "start_time")
  @Nullable
  private Date startTime;

  @ColumnInfo(name = "end_time")
  @Nullable
  private Date endTime;

  @ColumnInfo(name = "minutes")
  private long minutes;

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

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean finished) {
    isFinished = finished;
  }

  @Nullable
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  @Nullable
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
