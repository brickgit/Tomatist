package com.brickgit.tomatist.data.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;

@Data
@Entity(tableName = "projects")
public class Project {
  @PrimaryKey(autoGenerate = true)
  private long projectId;

  @ColumnInfo(name = "title")
  private String title;

  @ColumnInfo(name = "order")
  private long order;
}
