/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.libraries.remixer;

/**
 * Base class for all Remixes that does not do any value checking. A remix takes care of calling a
 * callback when the value is changed. It does not support any sort of null values.
 *
 * <p><b>This class is not thread-safe and should only be used from the main thread.</b>
 */
public class Remix<T> extends RemixerItem {

  /**
   * The callback to be executed when the value is updated.
   */
  private final RemixCallback<T> callback;

  /**
   * Creates a new Remix.
   *
   * @param key The key to use to save to SharedPreferences. This needs to be unique across all
   *     Remixes.
   * @param title The name to display in the UI.
   * @param defaultValue The default value for this Remix.
   * @param callback A callback to execute when the value is updated. Can be {@code null}.
   * @param layoutId A layout to inflate when displaying this Remix in the UI.
   */
  // TODO(miguely): Add default value semantics to the defaultValue, currently it behaves mostly
  // as an initial value. It should be used in cases when the value is set to an invalid value from
  // SharedPreferences or Firebase.
  public Remix(
      String title,
      String key,
      T defaultValue,
      RemixCallback<T> callback,
      int layoutId) {
    super(title, key, layoutId);
    // TODO(miguely): pull this out of SharedPreferences.
    this.selectedValue = defaultValue;
    this.callback = callback;
  }

  /**
   * Makes sure the default value is valid for this remix and runs the callback if so. This must be
   * called as soon as the Remix is created.
   *
   * @throws IllegalArgumentException The currently selected value (or default value) is invalid for
   *     this Remix. See {@link #checkValue(Object)}.
   */
  public final void init() {
    checkValue(selectedValue);
    runCallback();
  }

  /**
   * The currently selected value.
   */
  private T selectedValue;

  public T getSelectedValue() {
    return selectedValue;
  }

  public Class getRemixType() {
    return selectedValue.getClass();
  }

  /**
   * Checks that the value passed in is a valid value, otherwise throws {@link
   * IllegalArgumentException}.
   *
   * @throws IllegalArgumentException An invalid value was passed in.
   */
  protected void checkValue(T value) {
    // No need to check anything in the base class.
  }

  /**
   * Sets the selected value to a new value.
   *
   * <p>This needs to be implemented in each of the remixes that extend this class, it should throw
   * an IllegalArgumentException if the value is invalid.
   *
   * @param newValue Value to set. Cannot be null.
   * @throws IllegalArgumentException {@code newValue} is an invalid value for this Remix.
   */
  public void setValue(T newValue) {
    checkValue(newValue);
    selectedValue = newValue;
    runCallback();
  }

  private void runCallback() {
    if (callback != null) {
      callback.onValueSet(this);
    }
  }

  /**
   * Convenience builder for Remix.
   *
   * <p>This builder assumes a few things for your convenience:
   * <ul>
   * <li>If the layout id is not set, the default layout will be used.
   * <li>If the title is not set, the key will be used as title
   * </ul>
   *
   * <p>On the other hand: key is mandatory. If it's missing, an {@link IllegalArgumentException}
   * will be thrown.
   */
  public static class Builder<T> {

    private String key;
    private String title;
    private T defaultValue;
    private RemixCallback<T> callback;
    private int layoutId = 0;

    public Builder() {}

    public Builder<T> setKey(String key) {
      this.key = key;
      return this;

    }

    public Builder<T> setTitle(String title) {
      this.title = title;
      return this;
    }

    public Builder<T> setDefaultValue(T defaultValue) {
      this.defaultValue = defaultValue;
      return this;
    }

    public Builder<T> setCallback(RemixCallback<T> callback) {
      this.callback = callback;
      return this;
    }

    public Builder<T> setLayoutId(int layoutId) {
      this.layoutId = layoutId;
      return this;
    }

    /**
     * Returns a new Remix created with the configuration stored in this builder instance.
     *
     * @throws IllegalArgumentException If key is missing
     */
    public Remix<T> buildAndInit() {
      if (key == null) {
        throw new IllegalArgumentException("key cannot be unset for AnyValueRemix");
      }
      if (title == null) {
        title = key;
      }
      Remix<T> remix = new Remix<T>(title, key, defaultValue, callback, layoutId);
      remix.init();
      return remix;
    }
  }
}
