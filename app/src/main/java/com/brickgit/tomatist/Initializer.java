package com.brickgit.tomatist;

import com.brickgit.tomatist.data.preferences.TomatistPreferences;

import androidx.fragment.app.FragmentActivity;

/** Created by Daniel Lin on 2019/3/16. */
public class Initializer {

  public static void firstLaunchSetup(FragmentActivity activity) {
    TomatistPreferences pref = TomatistPreferences.getInstance(activity);
    if (!pref.isFirstLaunched()) {
      return;
    }
    pref.setIsFirstLaunched(false);
  }
}
