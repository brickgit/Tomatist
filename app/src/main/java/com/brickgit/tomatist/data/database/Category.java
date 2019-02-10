package com.brickgit.tomatist.data.database;

import javax.annotation.Nullable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "categories",
    indices = {@Index("group_id")},
    foreignKeys =
        @ForeignKey(
            entity = CategoryGroup.class,
            parentColumns = "id",
            childColumns = "group_id",
            onDelete = ForeignKey.SET_NULL))
public class Category {
  @PrimaryKey(autoGenerate = true)
  @ColumnInfo(name = "id")
  private Long categoryId;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "group_id")
  @Nullable
  private Long categoryGroupId;

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Long getCategoryGroupId() {
    return categoryGroupId;
  }

  public void setCategoryGroupId(Long categoryGroupId) {
    this.categoryGroupId = categoryGroupId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Category)) return false;
    return this.categoryId == ((Category) obj).categoryId;
  }
}
