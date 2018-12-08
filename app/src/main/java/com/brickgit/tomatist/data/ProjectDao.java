package com.brickgit.tomatist.data;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ProjectDao {
  @Query("SELECT * FROM projects")
  List<Project> getProjects();

  @Insert
  void insertProject(Project project);

  @Delete
  void deleteProject(Project project);
}
