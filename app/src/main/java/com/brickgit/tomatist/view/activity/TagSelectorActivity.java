package com.brickgit.tomatist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.data.viewmodel.TagSelectorViewModel;
import com.brickgit.tomatist.view.tagselector.SelectedTagListAdapter;
import com.brickgit.tomatist.view.tagselector.SelectedTagRecyclerView;
import com.brickgit.tomatist.view.tagselector.TagListAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/3/18. */
public class TagSelectorActivity extends BaseActivity {

  protected TagSelectorViewModel mTagSelectorViewModel;

  private EditText mTagEditText;
  private SelectedTagRecyclerView mSelectedTagListView;
  private SelectedTagListAdapter mSelectedTagListAdapter;
  private RecyclerView mTagListView;
  private TagListAdapter mTagListAdapter;

  public static void start(Activity activity) {
    Intent intent = new Intent(activity, TagSelectorActivity.class);
    activity.startActivity(intent);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tag_selector);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.tag);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_tag).setOnClickListener((view) -> showAddTagDialog());

    mTagEditText = findViewById(R.id.tag_edit_text);
    mSelectedTagListView = findViewById(R.id.selected_tag_list);
    mTagListView = findViewById(R.id.tag_list);

    FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
    layoutManager.setFlexDirection(FlexDirection.ROW);
    layoutManager.setJustifyContent(JustifyContent.FLEX_START);
    mSelectedTagListView.setLayoutManager(layoutManager);
    mSelectedTagListAdapter = new SelectedTagListAdapter();
    mSelectedTagListAdapter.setOnTagClickListener(
        (tag) -> mTagSelectorViewModel.updateSelectedTags(tag));
    mSelectedTagListView.setAdapter(mSelectedTagListAdapter);

    mTagListView.setLayoutManager(new LinearLayoutManager(this));
    mTagListAdapter = new TagListAdapter();
    mTagListAdapter.setOnTagClickListener((tag) -> mTagSelectorViewModel.updateSelectedTags(tag));
    mTagListView.setAdapter(mTagListAdapter);

    mTagSelectorViewModel = ViewModelProviders.of(this).get(TagSelectorViewModel.class);
    mTagSelectorViewModel
        .getSelectedTagList()
        .observe(
            this,
            (tagList) -> {
              mSelectedTagListAdapter.updateTags(tagList);
              mTagListAdapter.updateSelectedTags(tagList);
            });
    mTagSelectorViewModel
        .getTagList()
        .observe(this, (tagList) -> mTagListAdapter.updateTags(tagList));
  }

  @Override
  public boolean onSupportNavigateUp() {
    onBackPressed();
    return true;
  }

  private void showAddTagDialog() {
    final EditText newTagTitleView = new EditText(this);

    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
    dialog.setTitle(getString(R.string.add_tag));
    dialog.setView(newTagTitleView);

    dialog.setPositiveButton(
        getString(R.string.action_add),
        (view, which) -> {
          String newTagTitle = newTagTitleView.getText().toString().trim();
          if (!newTagTitle.isEmpty()) {
            Tag newTag = new Tag(newTagTitle);
            mTagSelectorViewModel.insertTag(newTag);
          }
        });

    dialog.create().show();
  }
}
