package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Tag;
import com.google.common.base.Joiner;
import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/** Created by Daniel Lin on 2019/3/18. */
public class TagSelectorViewModel extends BaseViewModel {

  private MutableLiveData<List<Tag>> mSelectedTagList;
  private LiveData<List<Tag>> mTagList;

  public LiveData<List<Tag>> getSelectedTagList() {
    if (mSelectedTagList == null) {
      mSelectedTagList = new MutableLiveData<>();
      mSelectedTagList.setValue(new ArrayList<>());
    }
    return mSelectedTagList;
  }

  public void updateSelectedTags(Tag tag) {
    List<Tag> tags = mSelectedTagList.getValue();
    if (tags != null) {
      if (tags.contains(tag)) {
        tags.remove(tag);
      } else {
        tags.add(tag);
      }
      mSelectedTagList.setValue(tags);
    }
  }

  public LiveData<List<Tag>> getTagList() {
    if (mTagList == null) {
      mTagList = mDataRepository.getTags();
    }
    return mTagList;
  }

  public void insertTag(Tag tag) {
    mDataRepository.insertTag(tag);
  }

  public String getSelectedTagLstString() {
    List<Tag> tagList = mSelectedTagList.getValue();
    if (tagList != null && !tagList.isEmpty()) {
      Collection<String> tagNameList = Collections2.transform(tagList, (tag) -> tag.getId());
      return Joiner.on(",").join(tagNameList);
    }
    return "";
  }
}
