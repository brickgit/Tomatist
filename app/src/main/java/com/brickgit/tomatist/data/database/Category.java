package com.brickgit.tomatist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "categories",
    indices = {@Index("group_id")},
    foreignKeys =
        @ForeignKey(entity = CategoryGroup.class, parentColumns = "id", childColumns = "group_id"))
public class Category {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private long categoryId;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "group_id")
  private long categoryGroupId;

  public long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(long categoryId) {
    this.categoryId = categoryId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public long getCategoryGroupId() {
    return categoryGroupId;
  }

  public void setCategoryGroupId(long categoryGroupId) {
    this.categoryGroupId = categoryGroupId;
  }
}
