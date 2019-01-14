package com.brickgit.tomatist.data.database;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public abstract class ActivityDao {

  @Transaction
  public LiveData<List<Activity>> getActivitiesForDate(int year, int month, int day) {
    Calendar cFrom = Calendar.getInstance();
    cFrom.set(Calendar.YEAR, year);
    cFrom.set(Calendar.MONTH, month - 1);
    cFrom.set(Calendar.DAY_OF_MONTH, day);
    cFrom.set(Calendar.HOUR_OF_DAY, 0);
    cFrom.set(Calendar.MINUTE, 0);
    cFrom.set(Calendar.SECOND, 0);

    Calendar cTo = Calendar.getInstance();
    cTo.set(Calendar.YEAR, year);
    cTo.set(Calendar.MONTH, month - 1);
    cTo.set(Calendar.DAY_OF_MONTH, day);
    cTo.set(Calendar.HOUR_OF_DAY, 23);
    cTo.set(Calendar.MINUTE, 59);
    cTo.set(Calendar.SECOND, 59);

    return getActivitiesForDate(cFrom.getTime(), cTo.getTime());
  }

  @Insert
  public abstract void insertActivity(Activity activity);

  @Query("SELECT * FROM activities WHERE `start_time` BETWEEN :from AND :to ORDER BY `start_time`")
  protected abstract LiveData<List<Activity>> getActivitiesForDate(Date from, Date to);
}
