package com.brickgit.tomatist.view.tagselector;

import android.content.Context;
import android.util.AttributeSet;

import com.brickgit.tomatist.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/** Created by Daniel Lin on 2019/3/18. */
public class SelectedTagRecyclerView extends RecyclerView {

  public SelectedTagRecyclerView(@NonNull Context context) {
    this(context, null);
  }

  public SelectedTagRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SelectedTagRecyclerView(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  protected void onMeasure(int widthSpec, int heightSpec) {
    heightSpec =
        MeasureSpec.makeMeasureSpec(
            getResources().getDimensionPixelSize(R.dimen.tag_recycler_view_max_height),
            MeasureSpec.AT_MOST);
    super.onMeasure(widthSpec, heightSpec);
  }
}
