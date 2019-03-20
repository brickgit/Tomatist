package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.database.Tag;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

/** Created by Daniel Lin on 2019/3/18. */
public class TagSelectorViewModel extends BaseViewModel {

  private LiveData<List<Tag>> mTagList;

  private MutableLiveData<List<String>> mSelectedTagIdList = new MutableLiveData<>();
  private LiveData<Map<String, Tag>> mSelectedTagMap =
      Transformations.switchMap(
          mSelectedTagIdList,
          (selectedTagIdList) ->
              Transformations.map(
                  mDataRepository.getTags(selectedTagIdList),
                  (selectedTagList) -> {
                    Map<String, Tag> map = new HashMap<>();
                    for (Tag tag : selectedTagList) {
                      map.put(tag.getId(), tag);
                    }
                    return map;
                  }));

  public LiveData<List<Tag>> getTagList() {
    if (mTagList == null) {
      mTagList = mDataRepository.getTags();
    }
    return mTagList;
  }

  public void insertTag(Tag tag) {
    mDataRepository.insertTag(tag);
  }

  public LiveData<List<String>> getSelectedTagIdList() {
    if (mSelectedTagIdList.getValue() == null) {
      mSelectedTagIdList.setValue(new ArrayList<>());
    }
    return mSelectedTagIdList;
  }

  public LiveData<Map<String, Tag>> getSelectedTagMap() {
    return mSelectedTagMap;
  }

  public void updateSelectedTags(Tag tag) {
    if (mSelectedTagIdList.getValue() != null) {
      List<String> tagIdList = mSelectedTagIdList.getValue();
      if (tagIdList.contains(tag.getId())) {
        tagIdList.remove(tag.getId());
      } else {
        tagIdList.add(tag.getId());
      }
      mSelectedTagIdList.setValue(tagIdList);
    }
  }

  public String getSelectedTagIdLstString() {
    if (mSelectedTagIdList.getValue() != null) {
      List<String> tagIdList = mSelectedTagIdList.getValue();
      if (!tagIdList.isEmpty()) {
        return Joiner.on(",").join(tagIdList);
      }
    }
    return "";
  }

  public void setSelectedTagIdListString(String selectedTagIdListString) {
    List<String> tagIdList =
        new ArrayList<>(Splitter.on(",").trimResults().splitToList(selectedTagIdListString));
    mSelectedTagIdList.setValue(tagIdList);
  }
}
