package com.brickgit.tomatist.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Dao
public abstract class ActionDao {

  @Transaction
  public LiveData<List<Action>> getFinishedActionsForYear(int year) {
    Calendar cFrom = Calendar.getInstance();
    cFrom.set(Calendar.YEAR, year);
    cFrom.set(Calendar.MONTH, cFrom.getActualMinimum(Calendar.MONTH));
    cFrom.set(Calendar.DAY_OF_MONTH, cFrom.getActualMinimum(Calendar.DAY_OF_MONTH));
    cFrom.set(Calendar.HOUR_OF_DAY, cFrom.getActualMinimum(Calendar.HOUR_OF_DAY));
    cFrom.set(Calendar.MINUTE, cFrom.getActualMinimum(Calendar.MINUTE));
    cFrom.set(Calendar.SECOND, cFrom.getActualMinimum(Calendar.SECOND));

    Calendar cTo = Calendar.getInstance();
    cTo.set(Calendar.YEAR, year);
    cTo.set(Calendar.MONTH, cTo.getActualMaximum(Calendar.MONTH));
    cTo.set(Calendar.DAY_OF_MONTH, cTo.getActualMaximum(Calendar.DAY_OF_MONTH));
    cTo.set(Calendar.HOUR_OF_DAY, cTo.getActualMaximum(Calendar.HOUR_OF_DAY));
    cTo.set(Calendar.MINUTE, cTo.getActualMaximum(Calendar.MINUTE));
    cTo.set(Calendar.SECOND, cTo.getActualMaximum(Calendar.SECOND));

    return getFinishedActionsForDate(cFrom.getTime(), cTo.getTime());
  }

  @Transaction
  public LiveData<List<Action>> getFinishedActionsForMonth(int year, int month) {
    Calendar cFrom = Calendar.getInstance();
    cFrom.set(Calendar.YEAR, year);
    cFrom.set(Calendar.MONTH, month);
    cFrom.set(Calendar.DAY_OF_MONTH, cFrom.getActualMinimum(Calendar.DAY_OF_MONTH));
    cFrom.set(Calendar.HOUR_OF_DAY, cFrom.getActualMinimum(Calendar.HOUR_OF_DAY));
    cFrom.set(Calendar.MINUTE, cFrom.getActualMinimum(Calendar.MINUTE));
    cFrom.set(Calendar.SECOND, cFrom.getActualMinimum(Calendar.SECOND));

    Calendar cTo = Calendar.getInstance();
    cTo.set(Calendar.YEAR, year);
    cTo.set(Calendar.MONTH, month);
    cTo.set(Calendar.DAY_OF_MONTH, cTo.getActualMaximum(Calendar.DAY_OF_MONTH));
    cTo.set(Calendar.HOUR_OF_DAY, cTo.getActualMaximum(Calendar.HOUR_OF_DAY));
    cTo.set(Calendar.MINUTE, cTo.getActualMaximum(Calendar.MINUTE));
    cTo.set(Calendar.SECOND, cTo.getActualMaximum(Calendar.SECOND));

    return getFinishedActionsForDate(cFrom.getTime(), cTo.getTime());
  }

  @Transaction
  public LiveData<List<Action>> getFinishedActionsForDate(int year, int month, int day) {
    Calendar cFrom = Calendar.getInstance();
    cFrom.set(Calendar.YEAR, year);
    cFrom.set(Calendar.MONTH, month);
    cFrom.set(Calendar.DAY_OF_MONTH, day);
    cFrom.set(Calendar.HOUR_OF_DAY, cFrom.getActualMinimum(Calendar.HOUR_OF_DAY));
    cFrom.set(Calendar.MINUTE, cFrom.getActualMinimum(Calendar.MINUTE));
    cFrom.set(Calendar.SECOND, cFrom.getActualMinimum(Calendar.SECOND));

    Calendar cTo = Calendar.getInstance();
    cTo.set(Calendar.YEAR, year);
    cTo.set(Calendar.MONTH, month);
    cTo.set(Calendar.DAY_OF_MONTH, day);
    cTo.set(Calendar.HOUR_OF_DAY, cTo.getActualMaximum(Calendar.HOUR_OF_DAY));
    cTo.set(Calendar.MINUTE, cTo.getActualMaximum(Calendar.MINUTE));
    cTo.set(Calendar.SECOND, cTo.getActualMaximum(Calendar.SECOND));

    return getFinishedActionsForDate(cFrom.getTime(), cTo.getTime());
  }

  @Transaction
  public LiveData<List<Action>> getActionsForDate(int year, int month, int day) {
    Calendar cFrom = Calendar.getInstance();
    cFrom.set(Calendar.YEAR, year);
    cFrom.set(Calendar.MONTH, month);
    cFrom.set(Calendar.DAY_OF_MONTH, day);
    cFrom.set(Calendar.HOUR_OF_DAY, cFrom.getActualMinimum(Calendar.HOUR_OF_DAY));
    cFrom.set(Calendar.MINUTE, cFrom.getActualMinimum(Calendar.MINUTE));
    cFrom.set(Calendar.SECOND, cFrom.getActualMinimum(Calendar.SECOND));

    Calendar cTo = Calendar.getInstance();
    cTo.set(Calendar.YEAR, year);
    cTo.set(Calendar.MONTH, month);
    cTo.set(Calendar.DAY_OF_MONTH, day);
    cTo.set(Calendar.HOUR_OF_DAY, cTo.getActualMaximum(Calendar.HOUR_OF_DAY));
    cTo.set(Calendar.MINUTE, cTo.getActualMaximum(Calendar.MINUTE));
    cTo.set(Calendar.SECOND, cTo.getActualMaximum(Calendar.SECOND));

    return getActionsForDate(cFrom.getTime(), cTo.getTime());
  }

  @Query("SELECT * FROM `actions` WHERE start_time is null")
  public abstract LiveData<List<Action>> getUnfinishedActions();

  @Query("SELECT * FROM `actions` WHERE id = :id")
  public abstract LiveData<Action> getAction(String id);

  @Insert
  public abstract void insertAction(Action action);

  @Update
  public abstract void updateAction(Action action);

  @Delete
  public abstract void deleteAction(Action action);

  @Query("SELECT * FROM `actions` WHERE start_time BETWEEN :from AND :to ORDER BY start_time")
  protected abstract LiveData<List<Action>> getActionsForDate(Date from, Date to);

  @Query(
      "SELECT * FROM `actions` WHERE is_finished = 1 AND start_time BETWEEN :from AND :to ORDER BY start_time")
  protected abstract LiveData<List<Action>> getFinishedActionsForDate(Date from, Date to);
}
