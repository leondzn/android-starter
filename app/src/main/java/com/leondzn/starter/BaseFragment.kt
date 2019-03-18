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

package com.leondzn.starter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseFragment<B : ViewDataBinding, VM : BaseViewModel> : Fragment(), HasSupportFragmentInjector {
  var viewDataBinding: B? = null
    private set
  var viewModel: VM? = null
    private set

  private var compositeDisposable: CompositeDisposable? = null

  protected abstract val layoutResource: Int

  protected abstract val bindingVariable: Int

  @Inject
  @JvmField
  internal var injector: DispatchingAndroidInjector<Fragment>? = null

  protected abstract fun createViewModel(): VM

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onAttach(context: Context?) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    this.viewDataBinding = DataBindingUtil.inflate(inflater, layoutResource, container, false)
    this.viewModel = createViewModel()
    return this.viewDataBinding!!.root
  }

  override fun onDestroyView() {
    super.onDestroyView()
    if (this.compositeDisposable != null && !this.compositeDisposable!!.isDisposed) {
      this.compositeDisposable!!.dispose()
      this.compositeDisposable = null
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    this.viewDataBinding!!.setVariable(bindingVariable, this.viewModel)
    listenToObservables()
  }

  override fun onDestroy() {
    super.onDestroy()
    if (this.compositeDisposable != null && !this.compositeDisposable!!.isDisposed) {
      this.compositeDisposable!!.dispose()
    }
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment>? {
    return this.injector
  }

  protected fun listenToObservables() {}

  protected fun observe(disposable: Disposable) {
    if (this.compositeDisposable == null) {
      this.compositeDisposable = CompositeDisposable()
    }
    this.compositeDisposable!!.add(disposable)
  }
}
