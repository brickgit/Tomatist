package com.brickgit.tomatist.data;

import androidx.room.RoomDatabase;

/** Created by Daniel Lin on 2018/10/13. */
@androidx.room.Database(
    entities = {Project.class, Task.class},
    version = 1)
public abstract class Database extends RoomDatabase {
  public abstract ProjectDao projectDao();

  public abstract TaskDao taskDao();
}
