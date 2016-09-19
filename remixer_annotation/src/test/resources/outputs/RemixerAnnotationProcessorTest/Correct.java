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

package somepackage;

import com.google.android.libraries.remixer.ItemListRemix;
import com.google.android.libraries.remixer.RangeRemix;
import com.google.android.libraries.remixer.Remix;
import com.google.android.libraries.remixer.RemixCallback;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.Trigger;
import com.google.android.libraries.remixer.annotation.RemixerBinder;
import java.lang.Boolean;
import java.lang.Integer;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.String;
import java.util.ArrayList;

/**
 * This class was generated by RemixerAnnotationProcessor */
class Correct_RemixerBinder implements RemixerBinder.Binder<Correct> {
  @Override
  public void bindInstance(Correct activity) {
    Remixer remixer;
    if (activity.remixer == null) {
      remixer = new Remixer();
    } else {
      remixer = activity.remixer;
    }
    Generated_setBoolean setBoolean_callback = new Generated_setBoolean(activity);
    Remix<Boolean> setBoolean_remix = new Remix<Boolean>("setBoolean", "setBoolean", false, setBoolean_callback, 0);
    setBoolean_remix.init();
    remixer.addItem(setBoolean_remix);
    Generated_setInt setInt_callback = new Generated_setInt(activity);
    RangeRemix setInt_remix = new RangeRemix("setInt", "setInt", 0, 0, 100, 1, setInt_callback, 0);
    setInt_remix.init();
    remixer.addItem(setInt_remix);
    Generated_setString setString_callback = new Generated_setString(activity);
    Remix<String> setString_remix = new Remix<String>("setString", "setString", "", setString_callback, 0);
    setString_remix.init();
    remixer.addItem(setString_remix);
    ArrayList<String> setStringList_remix_list = new ArrayList<String>();
    setStringList_remix_list.add("hello");
    setStringList_remix_list.add("world");
    Generated_setStringList setStringList_callback = new Generated_setStringList(activity);
    ItemListRemix<String> setStringList_remix = new ItemListRemix<String>("setStringList", "setStringList", "hello", setStringList_remix_list, setStringList_callback, 0);
    setStringList_remix.init();
    remixer.addItem(setStringList_remix);
    Generated_pullTrigger pullTrigger_callback = new Generated_pullTrigger(activity);
    Trigger pullTrigger_trigger = new Trigger("pullTrigger", "pullTrigger", pullTrigger_callback, 0);
    remixer.addItem(pullTrigger_trigger);
    activity.remixer = remixer;
  }

  static class Generated_setBoolean implements RemixCallback<Boolean> {
    private final Correct activity;

    Generated_setBoolean(Correct activity) {
      this.activity = activity;
    }

    @Override
    public void onValueSet(Remix<Boolean> remix) {
      activity.setBoolean(remix.getSelectedValue());
    }
  }

  static class Generated_setInt implements RemixCallback<Integer> {
    private final Correct activity;

    Generated_setInt(Correct activity) {
      this.activity = activity;
    }

    @Override
    public void onValueSet(Remix<Integer> remix) {
      activity.setInt(remix.getSelectedValue());
    }
  }

  static class Generated_setString implements RemixCallback<String> {
    private final Correct activity;

    Generated_setString(Correct activity) {
      this.activity = activity;
    }

    @Override
    public void onValueSet(Remix<String> remix) {
      activity.setString(remix.getSelectedValue());
    }
  }

  static class Generated_setStringList implements RemixCallback<String> {
    private final Correct activity;

    Generated_setStringList(Correct activity) {
      this.activity = activity;
    }

    @Override
    public void onValueSet(Remix<String> remix) {
      activity.setStringList(remix.getSelectedValue());
    }
  }

  static class Generated_pullTrigger implements Runnable {
    private final Correct activity;

    Generated_pullTrigger(Correct activity) {
      this.activity = activity;
    }

    @Override
    public void run() {
      activity.pullTrigger();
    }
  }
}
