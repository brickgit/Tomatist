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
  LiveData<Category> getCategory(long id);

  @Query("SELECT * FROM categories WHERE group_id = :categoryGroupId")
  LiveData<List<Category>> getCategories(long categoryGroupId);

  @Query("SELECT * FROM categories")
  LiveData<List<Category>> getCategories();

  @Insert
  long insertCategory(Category category);

  @Insert
  long[] insertCategories(List<Category> categories);

  @Delete
  void deleteCategory(Category category);
}
