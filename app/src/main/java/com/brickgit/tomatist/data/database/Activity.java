package com.brickgit.tomatist.data.database;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "activities")
public class Activity {
  @PrimaryKey(autoGenerate = true)
  private long activityId;

  @ColumnInfo(name = "start_time")
  private Date startTime;

  @ColumnInfo(name = "end_time")
  private Date endTime;

  @ColumnInfo(name = "minutes")
  private long minutes;

  @ColumnInfo(name = "note")
  private String note;

  public long getActivityId() {
    return activityId;
  }

  public void setActivityId(long activityId) {
    this.activityId = activityId;
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
}
