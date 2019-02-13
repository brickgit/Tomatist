package com.brickgit.tomatist.view.tasklist;

import android.os.Parcel;
import android.os.Parcelable;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListTaskViewData implements Parcelable {

  private String mTaskTitle;
  private Long mTaskId;

  public TaskListTaskViewData(String taskTitle, Long taskId) {
    mTaskTitle = taskTitle;
    mTaskId = taskId;
  }

  public String getTaskTitle() {
    return mTaskTitle;
  }

  public void setTaskTitle(String taskTitle) {
    this.mTaskTitle = taskTitle;
  }

  public Long getTaskId() {
    return mTaskId;
  }

  public void setTaskId(Long mTaskId) {
    this.mTaskId = mTaskId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(mTaskTitle);
    parcel.writeLong(mTaskId);
  }

  public static final Parcelable.Creator<TaskListTaskViewData> CREATOR =
      new Creator<TaskListTaskViewData>() {
        @Override
        public TaskListTaskViewData[] newArray(int size) {
          return new TaskListTaskViewData[size];
        }

        @Override
        public TaskListTaskViewData createFromParcel(Parcel parcel) {
          return new TaskListTaskViewData(parcel.readString(), parcel.readLong());
        }
      };
}
