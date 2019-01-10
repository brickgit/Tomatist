package com.brickgit.tomatist.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class ProjectDao {
  @Query("SELECT * FROM projects ORDER BY `order`")
  public abstract LiveData<List<Project>> getProjects();

  @Transaction
  public void insertProject(Project project) {
    long id = insertProjectWithoutOrder(project);
    project.setProjectId(id);
    project.setOrder(id);
    updateProject(project);
  }

  @Update
  public abstract void updateProject(Project project);

  @Update
  public abstract void updateProjects(List<Project> projects);

  @Delete
  public abstract void deleteProject(Project project);

  @Insert
  protected abstract long insertProjectWithoutOrder(Project project);
}
