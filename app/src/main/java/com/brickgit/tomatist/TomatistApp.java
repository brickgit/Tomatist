package com.brickgit.tomatist;

import android.app.Application;

import com.brickgit.tomatist.data.database.DatabaseLoader;

/** Created by Daniel Lin on 2018/10/15. */
public class TomatistApp extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    DatabaseLoader.init(this);

    Initializer.firstLaunchSetup(this);
  }
}
