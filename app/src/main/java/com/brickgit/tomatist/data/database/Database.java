package com.brickgit.tomatist.data.database;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/** Created by Daniel Lin on 2018/10/13. */
@androidx.room.Database(
    entities = {Activity.class, Category.class, CategoryGroup.class},
    version = 1)
@TypeConverters({DataConverters.class})
public abstract class Database extends RoomDatabase {

  public abstract ActivityDao activityDao();

  public abstract CategoryGroupDao categoryGroupDao();

  public abstract CategoryDao categoryDao();
}
