package com.brickgit.tomatist.data.database;

import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/** Created by Daniel Lin on 2018/10/13. */
@androidx.room.Database(
    entities = {Action.class, Tag.class},
    version = 1)
@TypeConverters({DataConverters.class})
public abstract class Database extends RoomDatabase {

  public abstract ActionDao actionDao();

  public abstract TagDao tagDao();
}
