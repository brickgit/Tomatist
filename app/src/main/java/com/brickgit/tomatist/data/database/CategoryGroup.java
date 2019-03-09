package com.brickgit.tomatist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category_groups")
public class CategoryGroup {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private Long id;

  @ColumnInfo(name = "title")
  private String title;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
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
    if (!(obj instanceof CategoryGroup)) return false;
    return this.id == ((CategoryGroup) obj).id;
  }
}
