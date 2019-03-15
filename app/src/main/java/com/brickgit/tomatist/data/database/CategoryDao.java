package com.brickgit.tomatist.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface CategoryDao {

  @Query("SELECT * FROM categories WHERE id = :id")
  LiveData<Category> getCategory(String id);

  @Query("SELECT * FROM categories WHERE group_id = :groupId")
  LiveData<List<Category>> getCategories(String groupId);

  @Query("SELECT * FROM categories")
  LiveData<List<Category>> getCategories();

  @Insert
  void insertCategory(Category category);

  @Insert
  void insertCategories(List<Category> categories);

  @Delete
  void deleteCategory(Category category);
}
