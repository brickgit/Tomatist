package com.brickgit.tomatist.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.data.viewmodel.TagSelectorViewModel;
import com.brickgit.tomatist.view.ListTouchHelperCallback;
import com.brickgit.tomatist.view.tagselector.SelectedTagListAdapter;
import com.brickgit.tomatist.view.tagselector.SelectedTagRecyclerView;
import com.brickgit.tomatist.view.tagselector.TagListAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/3/18. */
public class TagSelectorActivity extends BaseActivity {

  public static final String SELECTED_TAG_LIST = "SELECTED_TAG_LIST";

  protected TagSelectorViewModel mTagSelectorViewModel;
  private List<Tag> mTagList = new ArrayList<>();

  private View mRootView;
  private EditText mTagEditText;
  private SelectedTagRecyclerView mSelectedTagListView;
  private SelectedTagListAdapter mSelectedTagListAdapter;
  private RecyclerView mTagListView;
  private TagListAdapter mTagListAdapter;

  public static void startForResult(
      Activity activity, int requestCoe, List<String> selectedTagIdList) {
    Intent intent = new Intent(activity, TagSelectorActivity.class);
    intent.putExtra(SELECTED_TAG_LIST, Joiner.on(",").join(selectedTagIdList));
    activity.startActivityForResult(intent, requestCoe);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_tag_selector);

    mRootView = findViewById(R.id.root_view);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle(R.string.tag);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);

    findViewById(R.id.add_tag).setOnClickListener((view) -> showAddTagDialog());

    mTagEditText = findViewById(R.id.tag_edit_text);
    mTagEditText.addTextChangedListener(
        new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {}

          @Override
          public void afterTextChanged(Editable s) {
            mTagListAdapter.updateFilterString(s.toString().trim());
          }
        });
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
    ItemTouchHelper.Callback callback =
        new ListTouchHelperCallback((position) -> removeTag(mTagList.get(position)));
    ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
    touchHelper.attachToRecyclerView(mTagListView);

    mTagSelectorViewModel = ViewModelProviders.of(this).get(TagSelectorViewModel.class);
    mTagSelectorViewModel
        .getTagList()
        .observe(
            this,
            (tagList) -> {
              mTagList.clear();
              mTagList.addAll(tagList);
              mTagListAdapter.updateTagList(mTagList);
            });
    mTagSelectorViewModel
        .getSelectedTagIdList()
        .observe(this, (tagIdList) -> mSelectedTagListAdapter.updateTagIds(tagIdList));
    mTagSelectorViewModel
        .getSelectedTagMap()
        .observe(
            this,
            (tagMap) -> {
              mSelectedTagListAdapter.updateTagMap(tagMap);
              mTagListAdapter.updateSelectedTagMap(tagMap);
            });

    String selectedTagIdListString = getIntent().getStringExtra(SELECTED_TAG_LIST);
    if (selectedTagIdListString != null && !selectedTagIdListString.isEmpty()) {
      mTagSelectorViewModel.setSelectedTagIdListString(selectedTagIdListString);
    }
  }

  @Override
  public boolean onSupportNavigateUp() {
    setResult(RESULT_CANCELED);
    onBackPressed();
    return true;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_tag_selector, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_add:
        selectTags();
        return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void removeTag(Tag tag) {
    if (tag != null) {
      mTagSelectorViewModel.deleteTag(tag);
      showTagDeletedConfirmation(tag);
    }
  }

  private void showTagDeletedConfirmation(Tag tag) {
    Snackbar.make(mRootView, R.string.action_deleted, Snackbar.LENGTH_SHORT)
        .setAction(R.string.undo, (view) -> mTagSelectorViewModel.insertTag(tag))
        .show();
  }

  private void selectTags() {
    Intent intent = new Intent();
    intent.putExtra(SELECTED_TAG_LIST, mTagSelectorViewModel.getSelectedTagIdLstString());
    setResult(RESULT_OK, intent);
    finish();
  }

  private void showAddTagDialog() {
    final EditText newTagTitleView = new EditText(this);
    newTagTitleView.setSingleLine();

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
