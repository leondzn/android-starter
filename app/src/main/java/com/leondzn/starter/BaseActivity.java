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

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseActivity<VM extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity implements HasSupportFragmentInjector {
  @Inject
  DispatchingAndroidInjector<Fragment> injector;

  private B viewDataBinding;
  private VM viewModel;
  private CompositeDisposable compositeDisposable;

  protected abstract VM createViewModel();

  protected abstract int getLayoutResource();

  protected abstract int getViewModelBindingVariable();

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    this.viewDataBinding = DataBindingUtil.setContentView(this, getLayoutResource());
    this.viewModel = createViewModel();
    this.viewDataBinding.setVariable(getViewModelBindingVariable(), this.viewModel);

    listenToObservables();
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

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (this.compositeDisposable != null && !this.compositeDisposable.isDisposed()) {
      this.compositeDisposable.dispose();
    }
  }

  protected void observe(Disposable observable) {
    if (this.compositeDisposable == null) {
      this.compositeDisposable = new CompositeDisposable();
    }

    this.compositeDisposable.add(observable);
  }

  protected void listenToObservables() {
  }
}
