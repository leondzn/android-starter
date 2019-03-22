/*
 * Copyright (C) 2019 Leonard Dizon.
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

package com.snekbyte.starter

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

import javax.inject.Inject

import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseActivity<VM : BaseViewModel, B : ViewDataBinding> : AppCompatActivity(), HasSupportFragmentInjector {
  @Inject
  @JvmField
  internal var injector: DispatchingAndroidInjector<Fragment>? = null

  var viewDataBinding: B? = null
    private set
  var viewModel: VM? = null
    private set
  private var compositeDisposable: CompositeDisposable? = null

  protected abstract val layoutResource: Int

  protected abstract val viewModelBindingVariable: Int

  protected abstract fun createViewModel(): VM

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    this.viewDataBinding = DataBindingUtil.setContentView(this, layoutResource)
    this.viewModel = createViewModel()
    this.viewDataBinding!!.setVariable(viewModelBindingVariable, this.viewModel)

    listenToObservables()
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
    return this.injector
  }

  override fun onDestroy() {
    super.onDestroy()
    if (this.compositeDisposable != null && !this.compositeDisposable!!.isDisposed) {
      this.compositeDisposable!!.dispose()
    }
  }

  protected fun observe(observable: Disposable) {
    if (this.compositeDisposable == null) {
      this.compositeDisposable = CompositeDisposable()
    }

    this.compositeDisposable!!.add(observable)
  }

  protected fun listenToObservables() {}
}
