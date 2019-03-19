package com.brickgit.tomatist.data.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/** Created by Daniel Lin on 2019/3/18. */
@Entity(tableName = "tags")
public class Tag {
  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  private String id;

  @ColumnInfo(name = "title")
  private String title;

  public Tag(String title) {
    id = KeyGenerator.gen("TAG");
    this.title = title;
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

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Tag)) return false;
    return this.id.equals(((Tag) obj).id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
