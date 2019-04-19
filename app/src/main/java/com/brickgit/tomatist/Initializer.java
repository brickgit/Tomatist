package com.brickgit.tomatist;

import android.content.Context;

import com.brickgit.tomatist.data.preferences.TomatistPreferences;

/** Created by Daniel Lin on 2019/3/16. */
public class Initializer {

  public static void firstLaunchSetup(Context context) {
    TomatistPreferences pref = TomatistPreferences.getInstance(context);
    if (!pref.isFirstLaunched()) {
      return;
    }
    pref.setIsFirstLaunched(false);
  }
}
