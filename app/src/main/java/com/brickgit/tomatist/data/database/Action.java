package com.brickgit.tomatist.data.database;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "actions",
    indices = {@Index("category_id")},
    foreignKeys = {
      @ForeignKey(
          entity = Category.class,
          parentColumns = "id",
          childColumns = "category_id",
          onDelete = ForeignKey.SET_NULL)
    })
public class Action {
  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  private String id;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "is_finished")
  private boolean isFinished;

  @ColumnInfo(name = "start_time")
  private Date startTime;

  @ColumnInfo(name = "end_time")
  private Date endTime;

  @ColumnInfo(name = "note")
  private String note;

  @ColumnInfo(name = "category_id")
  @Nullable
  private String categoryId;

  public Action() {
    id = KeyGenerator.gen("ACT");
    Calendar today = Calendar.getInstance();
    startTime = today.getTime();
    endTime = today.getTime();
  }

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
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

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  @Nullable
  public String getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(@Nullable String categoryId) {
    this.categoryId = categoryId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Action)) return false;
    return this.id.equals(((Action) obj).id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
