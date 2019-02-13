package com.brickgit.tomatist.view.tasklist;

import android.os.Parcel;
import android.os.Parcelable;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListTaskViewData implements Parcelable {

  private String mTaskTitle;

  public TaskListTaskViewData(String taskTitle) {
    mTaskTitle = taskTitle;
  }

  public String getTaskTitle() {
    return mTaskTitle;
  }

  public void setTaskTitle(String taskTitle) {
    this.mTaskTitle = taskTitle;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int i) {
    parcel.writeString(mTaskTitle);
  }

  public static final Parcelable.Creator<TaskListTaskViewData> CREATOR =
      new Creator<TaskListTaskViewData>() {
        @Override
        public TaskListTaskViewData[] newArray(int size) {
          return new TaskListTaskViewData[size];
        }

        @Override
        public TaskListTaskViewData createFromParcel(Parcel parcel) {
          return new TaskListTaskViewData(parcel.readString());
        }
      };
}
