package com.brickgit.tomatist.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/** Created by Daniel Lin on 2019/3/18. */
@Dao
public interface TagDao {

  @Query("SELECT * FROM tags WHERE id = :id")
  LiveData<Tag> getTag(String id);

  @Query("SELECT * FROM tags")
  LiveData<List<Tag>> getTags();

  @Insert
  void insertTag(Tag tag);

  @Insert
  void insertTags(List<Tag> tags);

  @Delete
  void deleteTag(Tag tag);
}
