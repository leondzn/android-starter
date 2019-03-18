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

import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.databinding.Observable
import android.databinding.ObservableBoolean
import android.databinding.PropertyChangeRegistry

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel(), Observable {
  @Transient
  private var callbacks: PropertyChangeRegistry? = null

  val isLoading = ObservableBoolean()

  private var compositeDisposable: CompositeDisposable? = null

  override fun onCleared() {
    super.onCleared()
    if (this.compositeDisposable != null && !this.compositeDisposable!!.isDisposed) {
      this.compositeDisposable!!.dispose()
    }
  }

  override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
    synchronized(this) {
      if (this.callbacks == null) {
        this.callbacks = PropertyChangeRegistry()
      }
    }
    this.callbacks!!.add(callback)
  }

  override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback) {
    synchronized(this) {
      if (this.callbacks == null) {
        return
      }
    }
    this.callbacks!!.remove(callback)
  }

  /**
   * Notifies listeners that all properties of this instance have changed.
   */
  fun notifyChange() {
    synchronized(this) {
      if (this.callbacks == null) {
        return
      }
    }
    this.callbacks!!.notifyCallbacks(this, 0, null)
  }

  /**
   * Notifies listeners that a specific property has changed. The getter for the property
   * that changes should be marked with [Bindable] to generate a field in
   * `BR` to be used as `fieldId`.
   *
   * @param fieldId The generated BR id for the Bindable field.
   */
  fun notifyPropertyChanged(fieldId: Int) {
    synchronized(this) {
      if (this.callbacks == null) {
        return
      }
    }
    this.callbacks!!.notifyCallbacks(this, fieldId, null)
  }

  fun observe(subscriber: Disposable) {
    if (this.compositeDisposable == null) {
      this.compositeDisposable = CompositeDisposable()
    }
    this.compositeDisposable!!.add(subscriber)
  }
}
