package com.brickgit.tomatist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_groups")
public class CategoryGroup {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private Long categoryGroupId;

  @ColumnInfo(name = "title")
  private String title;

  public Long getCategoryGroupId() {
    return categoryGroupId;
  }

  public void setCategoryGroupId(Long categoryGroupId) {
    this.categoryGroupId = categoryGroupId;
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
    if (!(obj instanceof CategoryGroup)) return false;
    return this.categoryGroupId == ((CategoryGroup) obj).categoryGroupId;
  }
}
