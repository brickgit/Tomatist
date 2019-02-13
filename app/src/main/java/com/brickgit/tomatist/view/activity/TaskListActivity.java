package com.brickgit.tomatist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Category;
import com.brickgit.tomatist.data.database.CategoryGroup;
import com.brickgit.tomatist.data.database.Task;
import com.brickgit.tomatist.view.tasklist.TaskListAdapter;
import com.brickgit.tomatist.view.tasklist.TaskListCategoryViewData;
import com.brickgit.tomatist.view.tasklist.TaskListTaskViewData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/2/12. */
public class TaskListActivity extends BaseActivity {

  private RecyclerView mTaskList;

  private List<Task> mTasks = new ArrayList<>();
  private Map<Long, CategoryGroup> mCategoryGroups = new HashMap<>();
  private Map<Long, Category> mCategories = new HashMap<>();

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, TaskListActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_task_list);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.tasks);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_task).setOnClickListener((view) -> AddTaskActivity.start(this));

    mTaskList = findViewById(R.id.task_list);
    mTaskList.setLayoutManager(new LinearLayoutManager(this));
    TaskListAdapter adapter = new TaskListAdapter(new ArrayList<>());
    mTaskList.setAdapter(adapter);
    adapter.expandAll();

    mProjectViewModel
        .getTasks()
        .observe(
            this,
            (tasks) -> {
              mTasks.clear();
              mTasks.addAll(tasks);
              updateTaskList();
            });

    mCategoryViewModel
        .getCategoryGroups()
        .observe(
            this,
            (categoryGroups) -> {
              mCategoryGroups.clear();
              for (CategoryGroup categoryGroup : categoryGroups) {
                mCategoryGroups.put(categoryGroup.getCategoryGroupId(), categoryGroup);
              }
              updateTaskList();
            });

    mCategoryViewModel
        .getCategories()
        .observe(
            this,
            (categories) -> {
              mCategories.clear();
              for (Category category : categories) {
                mCategories.put(category.getCategoryId(), category);
              }
              updateTaskList();
            });
  }

  private void updateTaskList() {
    Map<String, List<TaskListTaskViewData>> map = new LinkedHashMap<>();
    String uncategorized = getString(R.string.uncategorized);
    map.put(uncategorized, new ArrayList<>());
    for (Task task : mTasks) {
      Category category = mCategories.get(task.getCategoryId());
      if (category == null) {
        map.get(uncategorized).add(new TaskListTaskViewData(task.getTitle()));
        continue;
      }

      CategoryGroup categoryGroup = mCategoryGroups.get(category.getCategoryGroupId());
      if (categoryGroup == null) {
        String key = uncategorized + " - " + category.getTitle();
        if (!map.containsKey(key)) map.put(key, new ArrayList<>());
        map.get(key).add(new TaskListTaskViewData(task.getTitle()));
        continue;
      }

      String key = categoryGroup.getTitle() + " - " + category.getTitle();
      if (!map.containsKey(key)) map.put(key, new ArrayList<>());
      map.get(key).add(new TaskListTaskViewData(task.getTitle()));
    }

    List<TaskListCategoryViewData> newTaskList = new ArrayList<>();
    for (String key : map.keySet()) {
      List<TaskListTaskViewData> tasks = map.get(key);
      if (!tasks.isEmpty()) {
        newTaskList.add(new TaskListCategoryViewData(key, map.get(key)));
      }
    }

    TaskListAdapter adapter = new TaskListAdapter(newTaskList);
    mTaskList.setAdapter(adapter);
    adapter.expandAll();
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }
}
