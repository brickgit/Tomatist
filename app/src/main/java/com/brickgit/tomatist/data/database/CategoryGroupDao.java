package com.brickgit.tomatist.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CategoryGroupDao {

  @Query("SELECT * FROM category_groups WHERE id = :id")
  LiveData<CategoryGroup> getCategoryGroup(long id);

  @Query("SELECT * FROM category_groups")
  LiveData<List<CategoryGroup>> getCategoryGroups();

  @Insert
  long insertCategoryGroup(CategoryGroup categoryGroup);

  @Delete
  void deleteCategoryGroup(CategoryGroup categoryGroup);
}
