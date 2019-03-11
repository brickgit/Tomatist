package com.brickgit.tomatist.data.viewmodel;

import com.brickgit.tomatist.data.DataRepository;

import androidx.lifecycle.ViewModel;

/** Created by Daniel Lin on 2019/3/10. */
abstract class BaseViewModel extends ViewModel {

  DataRepository mDataRepository;

  BaseViewModel() {
    mDataRepository = DataRepository.getInstance();
  }
}
