package com.brickgit.tomatist.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class TomatistPreferences {

  private static volatile TomatistPreferences INSTANCE;

  private static String PREFERENCES_NAME = "com.brickgit.tomatist.preferences";
  private static String IS_FIRST_LAUNCHED = "is_first_launched";

  private SharedPreferences sharedPreferences;

  public static TomatistPreferences getInstance(Context context) {
    if (INSTANCE == null) {
      synchronized (TomatistPreferences.class) {
        if (INSTANCE == null) INSTANCE = new TomatistPreferences(context);
      }
    }
    return INSTANCE;
  }

  private TomatistPreferences(Context context) {
    sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
  }

  public boolean isFirstLaunched() {
    return sharedPreferences.getBoolean(IS_FIRST_LAUNCHED, true);
  }

  public void setIsFirstLaunched(boolean isFirstLaunched) {
    sharedPreferences.edit().putBoolean(IS_FIRST_LAUNCHED, isFirstLaunched).apply();
  }
}
