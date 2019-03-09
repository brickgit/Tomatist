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
  private Long id;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "group_id")
  @Nullable
  private Long groupId;

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

  public Long getGroupId() {
    return groupId;
  }

  public void setGroupId(Long groupId) {
    this.groupId = groupId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Category)) return false;
    return this.id == ((Category) obj).id;
  }
}
