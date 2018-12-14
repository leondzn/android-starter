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

import android.arch.lifecycle.ViewModel;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.PropertyChangeRegistry;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseViewModel extends ViewModel implements Observable {
  private transient PropertyChangeRegistry callbacks;

  public final ObservableBoolean isLoading = new ObservableBoolean();

  private CompositeDisposable compositeDisposable;

  @Override
  protected void onCleared() {
    super.onCleared();
    if (this.compositeDisposable != null && !this.compositeDisposable.isDisposed()) {
      this.compositeDisposable.dispose();
    }
  }

  @Override
  public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    synchronized (this) {
      if (this.callbacks == null) {
        this.callbacks = new PropertyChangeRegistry();
      }
    }
    this.callbacks.add(callback);
  }

  @Override
  public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
    synchronized (this) {
      if (this.callbacks == null) {
        return;
      }
    }
    this.callbacks.remove(callback);
  }

  /**
   * Notifies listeners that all properties of this instance have changed.
   */
  public void notifyChange() {
    synchronized (this) {
      if (this.callbacks == null) {
        return;
      }
    }
    this.callbacks.notifyCallbacks(this, 0, null);
  }

  /**
   * Notifies listeners that a specific property has changed. The getter for the property
   * that changes should be marked with {@link Bindable} to generate a field in
   * <code>BR</code> to be used as <code>fieldId</code>.
   *
   * @param fieldId The generated BR id for the Bindable field.
   */
  public void notifyPropertyChanged(int fieldId) {
    synchronized (this) {
      if (this.callbacks == null) {
        return;
      }
    }
    this.callbacks.notifyCallbacks(this, fieldId, null);
  }

  public void observe(Disposable subscriber) {
    if (this.compositeDisposable == null) {
      this.compositeDisposable = new CompositeDisposable();
    }
    this.compositeDisposable.add(subscriber);
  }
}
