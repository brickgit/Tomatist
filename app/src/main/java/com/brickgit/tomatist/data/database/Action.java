package com.brickgit.tomatist.data.database;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "actions")
public class Action {
  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  private String id;

  @ColumnInfo(name = "is_finished")
  private boolean isFinished;

  @ColumnInfo(name = "start_time")
  private Date startTime;

  @ColumnInfo(name = "end_time")
  private Date endTime;

  @ColumnInfo(name = "note")
  private String note;

  @ColumnInfo(name = "tags")
  private String tags;

  @Ignore private List<String> tagList;

  public Action() {
    id = KeyGenerator.gen("ACT");
    Calendar today = Calendar.getInstance();
    startTime = today.getTime();
    endTime = today.getTime();
    tagList = new ArrayList<>();
  }

  @NonNull
  public String getId() {
    return id;
  }

  public void setId(@NonNull String id) {
    this.id = id;
  }

  public boolean isFinished() {
    return isFinished;
  }

  public void setFinished(boolean isFinished) {
    this.isFinished = isFinished;
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

  String getTags() {
    return tags;
  }

  void setTags(String tags) {
    this.tags = tags;

    tagList.clear();
    if (tags != null && !tags.isEmpty()) {
      tagList.addAll(Splitter.on(",").trimResults().splitToList(tags));
    }
  }

  @NonNull
  public List<String> getTagList() {
    return tagList;
  }

  public void setTagList(List<String> tagList) {
    this.tagList.clear();
    this.tagList.addAll(tagList);
    if (tagList.isEmpty()) {
      tags = "";
    } else {
      tags = Joiner.on(",").join(tagList);
    }
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
