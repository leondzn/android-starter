/*
 * Copyright (C) 2018 Leonard Dizon.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.leondzn.starter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseFragment<B extends ViewDataBinding, VM extends BaseViewModel>
    extends Fragment implements HasSupportFragmentInjector {
  private B viewDataBinding;
  private VM viewModel;

  private CompositeDisposable compositeDisposable;

  protected abstract VM createViewModel();

  protected abstract int getLayoutResource();

  protected abstract int getBindingVariable();

  @Inject
  DispatchingAndroidInjector<Fragment> injector;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    this.viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutResource(), container, false);
    this.viewModel = createViewModel();
    return this.viewDataBinding.getRoot();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    if (this.compositeDisposable != null && !this.compositeDisposable.isDisposed()) {
      this.compositeDisposable.dispose();
      this.compositeDisposable = null;
    }
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    this.viewDataBinding.setVariable(getBindingVariable(), this.viewModel);
    listenToObservables();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    if (this.compositeDisposable != null && !this.compositeDisposable.isDisposed()) {
      this.compositeDisposable.dispose();
    }
  }

  @Override
  public AndroidInjector<Fragment> supportFragmentInjector() {
    return this.injector;
  }

  public B getViewDataBinding() {
    return this.viewDataBinding;
  }

  public VM getViewModel() {
    return this.viewModel;
  }

  protected void listenToObservables() {
  }

  protected void observe(Disposable disposable) {
    if (this.compositeDisposable == null) {
      this.compositeDisposable = new CompositeDisposable();
    }
    this.compositeDisposable.add(disposable);
  }
}
