package com.brickgit.tomatist.data.database;

import android.content.Context;

import androidx.room.Room;

/** Created by Daniel Lin on 2018/10/15. */
public class DatabaseLoader {

  private static Database DATABASE;

  public static void init(Context context) {
    DATABASE =
        Room.databaseBuilder(context.getApplicationContext(), Database.class, "tomatist-db")
            .allowMainThreadQueries()
            .build();
  }

  public static Database getAppDatabase() {
    return DATABASE;
  }
}
