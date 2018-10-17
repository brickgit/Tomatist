package com.brickgit.tomatist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.brickgit.tomatist.data.Database;
import com.brickgit.tomatist.data.DatabaseLoader;
import com.brickgit.tomatist.data.Task;
import com.brickgit.tomatist.data.TaskDao;
import com.brickgit.tomatist.view.TaskListAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** A placeholder fragment containing a simple view. */
public class MainActivityFragment extends Fragment {

  private RecyclerView mTaskList;
  private LinearLayoutManager mLayoutManager;
  private TaskListAdapter mTaskListAdapter;

  private Database mDatabase;

  public MainActivityFragment() {}

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_main, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    mTaskList = view.findViewById(R.id.task_list);
    mTaskList.setHasFixedSize(true);

    mLayoutManager = new LinearLayoutManager(getActivity());
    mTaskList.setLayoutManager(mLayoutManager);

    mTaskListAdapter = new TaskListAdapter();
    mTaskList.setAdapter(mTaskListAdapter);

    mDatabase = DatabaseLoader.getAppDatabase();
    TaskDao dao = mDatabase.taskDao();
    List<Task> tasks = dao.getRootTasks();
    mTaskListAdapter.updateTasks(tasks);
    mTaskListAdapter.setOnTaskClickListener(
        new TaskListAdapter.OnTaskClickListener() {
          @Override
          public void onNewTaskLick() {
            showNewTaskDialog();
          }

          @Override
          public void onTaskClick(Task task) {
            showDeleteTaskDialog(task);
          }

          @Override
          public void onTaskCheck(Task task) {
            mTaskListAdapter.notifyDataSetChanged();
          }
        });
  }

  private void showNewTaskDialog() {
    final EditText editText = new EditText(getActivity());
    AlertDialog.Builder inputDialog = new AlertDialog.Builder(getActivity());
    inputDialog.setTitle("Adding task").setMessage("Please input task name.").setView(editText);
    inputDialog
        .setPositiveButton(
            "Add",
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                addTask(editText.getText().toString());
              }
            })
        .show();
  }

  private void addTask(String taskTitle) {
    Task task = new Task();
    task.setTitle(taskTitle);
    TaskDao dao = mDatabase.taskDao();
    dao.insertTask(task);
    List<Task> tasks = dao.getRootTasks();
    mTaskListAdapter.updateTasks(tasks);
  }

  private void showDeleteTaskDialog(final Task task) {
    final AlertDialog.Builder normalDialog = new AlertDialog.Builder(getActivity());
    normalDialog.setTitle("Deleting task");
    normalDialog.setMessage("Are you sure to delete " + task.getTitle() + "?");
    normalDialog.setPositiveButton(
        "Confirm",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
            deleteTask(task);
          }
        });
    normalDialog.setNegativeButton("Cancel", null);
    normalDialog.show();
  }

  private void deleteTask(Task task) {
    TaskDao dao = mDatabase.taskDao();
    dao.deleteTask(task);
    List<Task> tasks = dao.getRootTasks();
    mTaskListAdapter.updateTasks(tasks);
  }
}
