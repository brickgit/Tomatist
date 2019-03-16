package com.brickgit.tomatist.data.database;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
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
  @PrimaryKey
  @ColumnInfo(name = "id")
  @NonNull
  private String id;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "group_id")
  @Nullable
  private String groupId;

  public Category() {
    id = KeyGenerator.gen("CTG");
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

  @Nullable
  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(@Nullable String groupId) {
    this.groupId = groupId;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Category)) return false;
    return this.id.equals(((Category) obj).id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
