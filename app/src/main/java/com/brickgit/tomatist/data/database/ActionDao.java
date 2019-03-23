package com.brickgit.tomatist.data.database;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

@Dao
public abstract class ActionDao {

  @Transaction
  public LiveData<List<Action>> getFinishedActionsForDate(int year, int month, int day) {
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

    return getFinishedActionsForDate(cFrom.getTime(), cTo.getTime());
  }

  @Query("SELECT * FROM `actions` WHERE is_finished = 0")
  public abstract LiveData<List<Action>> getUnfinishedActions();

  @Query("SELECT * FROM `actions` WHERE id = :id")
  public abstract LiveData<Action> getAction(String id);

  @Insert
  public abstract void insertAction(Action action);

  @Update
  public abstract void updateAction(Action action);

  @Delete
  public abstract void deleteAction(Action action);

  @Query(
      "SELECT * FROM `actions` WHERE is_finished = 1 AND start_time BETWEEN :from AND :to ORDER BY start_time")
  protected abstract LiveData<List<Action>> getFinishedActionsForDate(Date from, Date to);
}
