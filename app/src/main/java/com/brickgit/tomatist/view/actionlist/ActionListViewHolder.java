package com.brickgit.tomatist.view.actionlist;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.brickgit.tomatist.R;
import com.brickgit.tomatist.data.database.Action;
import com.brickgit.tomatist.data.database.Tag;
import com.brickgit.tomatist.view.tagselector.SelectedTagListAdapter;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

public class ActionListViewHolder extends RecyclerView.ViewHolder {

  private static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

  private static RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

  private CheckBox mIsFinishedView;

  private View mHeaderView;
  private TextView mStartDateTime;
  private TextView mEndDateTime;

  private TextView mNoteView;

  private RecyclerView mTagListView;
  private SelectedTagListAdapter mTagListAdapter;

  private ExpandableLayout mButtons;

  private Action mAction;

  public ActionListViewHolder(
      View view,
      final ActionListAdapter.OnItemClickListener onItemClickListener,
      final ActionListAdapter.OnActionClickListener onActionClickListener) {
    super(view);

    mIsFinishedView = view.findViewById(R.id.is_finished_view);
    mIsFinishedView.setOnClickListener(
        (v) -> {
          if (onActionClickListener != null) {
            onActionClickListener.onCheckClick(mAction);
          }
        });

    mHeaderView = view.findViewById(R.id.header);
    mStartDateTime = view.findViewById(R.id.start_time);
    mEndDateTime = view.findViewById(R.id.end_time);

    mNoteView = view.findViewById(R.id.action_note);

    mTagListView = view.findViewById(R.id.tag_list);
    mTagListView.setRecycledViewPool(viewPool);
    mTagListView.setLayoutManager(
        new LinearLayoutManager(mTagListView.getContext(), LinearLayoutManager.HORIZONTAL, false));
    mTagListAdapter = new SelectedTagListAdapter();
    mTagListView.setAdapter(mTagListAdapter);

    mButtons = view.findViewById(R.id.buttons);
    mButtons.setOnExpansionUpdateListener(
        (expansionFraction, state) -> {
          if (state == ExpandableLayout.State.EXPANDING) {
            if (onActionClickListener != null) {
              onActionClickListener.onItemExpand(itemView);
            }
          }
        });

    itemView.setOnClickListener(
        (v) -> {
          toggle(true);
          if (onItemClickListener != null) {
            onItemClickListener.onItemClick(this);
          }
        });

    view.findViewById(R.id.edit)
        .setOnClickListener(
            (v) -> {
              if (onActionClickListener != null) {
                onActionClickListener.onEditClick(mAction);
              }
            });

    view.findViewById(R.id.copy)
        .setOnClickListener(
            (v) -> {
              if (onActionClickListener != null) {
                onActionClickListener.onCopyClick(mAction);
              }
            });

    view.findViewById(R.id.delete)
        .setOnClickListener(
            (v) -> {
              if (onActionClickListener != null) {
                onActionClickListener.onDeleteClick(mAction);
              }
            });
  }

  public void bind(Action action, Map<String, Tag> tags) {
    mAction = action;

    mIsFinishedView.setChecked(action.isFinished());

    mTagListAdapter.updateTagIds(action.getTagList());
    mTagListAdapter.updateTagMap(tags);

    if (action.getStartTime() != null) {
      mHeaderView.setVisibility(View.VISIBLE);
      mStartDateTime.setText(dateFormat.format(action.getStartTime()));
      mStartDateTime.setVisibility(View.VISIBLE);
      if (action.getEndTime() != null) {
        mEndDateTime.setText(dateFormat.format(action.getEndTime()));
        mEndDateTime.setVisibility(View.VISIBLE);
      } else {
        mEndDateTime.setText("00:00");
        mEndDateTime.setVisibility(View.GONE);
      }
    } else {
      mHeaderView.setVisibility(View.INVISIBLE);
      mStartDateTime.setText("00:00");
      mStartDateTime.setVisibility(View.INVISIBLE);
      mEndDateTime.setText("00:00");
      mEndDateTime.setVisibility(View.INVISIBLE);
    }

    mNoteView.setText(action.getNote());
  }

  public Action getAction() {
    return mAction;
  }

  public void toggle(boolean animate) {
    mButtons.toggle(animate);
  }

  public void collapse(boolean animate) {
    mButtons.collapse(animate);
  }

  public void expand(boolean animate) {
    mButtons.expand(animate);
  }
}
