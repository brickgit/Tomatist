package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;

import androidx.lifecycle.ViewModel;

/** Created by Daniel Lin on 2019/3/10. */
public abstract class BaseViewModel extends ViewModel {

  protected DataRepository mDataRepository;

  public BaseViewModel() {
    mDataRepository = DataRepository.getInstance();
  }
}
