package com.brickgit.tomatist.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

public class TomatistPreferences {

  private static volatile TomatistPreferences INSTANCE;

  private static final String PREFERENCES_NAME = "com.brickgit.tomatist.preferences";
  private static final String IS_FIRST_LAUNCHED = "is_first_launched";
  private static final String LAST_USED_CATEGORY_GROUP_ID = "last_used_category_group_id";
  private static final String LAST_USED_CATEGORY_ID = "last_used_category_id";

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

  public long lastUsedCategoryGroupId() {
    return sharedPreferences.getLong(LAST_USED_CATEGORY_GROUP_ID, 1);
  }

  public void setLastUsedCategoryGroupId(long categoryGroupId) {
    sharedPreferences.edit().putLong(LAST_USED_CATEGORY_GROUP_ID, categoryGroupId).apply();
  }

  public String lastUsedCategoryId() {
    return sharedPreferences.getString(LAST_USED_CATEGORY_ID, "");
  }

  public void setLastUsedCategoryId(String categoryId) {
    sharedPreferences.edit().putString(LAST_USED_CATEGORY_ID, categoryId).apply();
  }
}
