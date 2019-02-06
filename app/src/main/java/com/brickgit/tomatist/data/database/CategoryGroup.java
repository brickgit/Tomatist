package com.brickgit.tomatist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_groups")
public class CategoryGroup {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private long categoryGroupId;

  @ColumnInfo(name = "title")
  private String title;

  public long getCategoryGroupId() {
    return categoryGroupId;
  }

  public void setCategoryGroupId(long categoryGroupId) {
    this.categoryGroupId = categoryGroupId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
