package com.brickgit.tomatist.data.database;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "activities",
    indices = {@Index("category_id")},
    foreignKeys =
        @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "category_id"))
public class Activity {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private long activityId;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "start_time")
  private Date startTime;

  @ColumnInfo(name = "end_time")
  private Date endTime;

  @ColumnInfo(name = "minutes")
  private long minutes;

  @ColumnInfo(name = "note")
  private String note;

  @ColumnInfo(name = "category_id")
  private long categoryId;

  public long getActivityId() {
    return activityId;
  }

  public void setActivityId(long activityId) {
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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }
}
