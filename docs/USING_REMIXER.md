# Using Remixer

This page explains what you need to do to get Remixer running on your app. However, if you're the kind of person who learns by example, you may want to look at our [example app](https://github.com/material-foundation/material-remixer-android/tree/develop/remixer_example).

You can read our [javadoc for the current release (1.0)](https://jitpack.io/com/github/material-foundation/material-remixer-android/remixer/1.0/javadoc/index.html) and the [javadoc for HEAD](https://jitpack.io/com/github/material-foundation/material-remixer-android/remixer/develop-SNAPSHOT/javadoc/index.html) generated by jitpack.

## Dependencies and other requirements

Using gradle it's super easy to start using Remixer following these instructions.

In your main build.gradle file make sure you have the following dependencies and repositories set up:

```gradle
buildscript {
  dependencies {
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
  }
}

allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
```

And in your modules, apply the `android-apt` plugin and add the remixer dependencies:
```gradle
apply plugin: 'android-apt'

dependencies {
    compile 'com.github.material-foundation.material-remixer-android:remixer:1.0'
    provided 'com.github.material-foundation.material-remixer-android:remixer_annotation:1.0'
}
```

Notice the dependency on `remixer_annotation` is a `provided` clause instead of `compile`, this is on purpose as this is not a regular dependency but a compiler plugin.

**Important:** Once you start depending on Remixer, your build will fail if no `google-services.json` exists on your app module. This is because Remixer has the option of using firebase to store and sync values, and whether you use it or not the build system looks for that file. It can be an empty file or the one we provide in `remixer_example/src/main/google-services.json`.

## Initialize remixer

1. Subclass the `android.app.Application`.
2. [Declare it in your Android Manifest](https://developer.android.com/guide/topics/manifest/application-element.html#nm).
3. Call the `RemixerInitialization#initRemixer(Application)` method.
4. Set a synchronization mechanism for Remixer (you have a few options):
  - `com.google.android.libraries.remixer.sync.LocalValueSyncing`, this is the default (you don't need to set it if this is what you want), it doesn't persist any values but makes sure values sync across different contexts (Activities).
  - Recommended: `com.google.android.libraries.remixer.storage.LocalStorage`, this stores values locally in a SharedPreferences file.
  - `com.google.android.libraries.remixer.storage.FirebaseRemoteControllerSyncer`, this syncs values to and from a firebase instance to use it as a remote controller. Take a look at the [Firebase Remote Controller Set-up](CONFIGURE_FIREBASE.md) document for more information.

For example:

```java
import android.app.Application;
import com.google.android.libraries.remixer.Remixer;
import com.google.android.libraries.remixer.storage.LocalStorage;
import com.google.android.libraries.remixer.ui.RemixerInitialization;

class MyApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    RemixerInitialization.initRemixer(this);
    Remixer.getInstance().setSynchronizationMechanism(new LocalStorage(getApplicationContext()));
  }
}
```

## Define Variables

You can define variables in an activity by writing methods that take one argument of the correct type and annotate them. The methods contain your logic to handle changes to these variables (update the UI accordingly).

You can rest assured those methods will run in the main UI thread.

There are a few very simple examples here, but you should look at the [example activity](https://github.com/material-foundation/material-remixer-android/blob/develop/remixer_example/src/main/java/com/google/android/apps/remixer/TransactionListActivity.java) and [documentation for these annotations](https://github.com/material-foundation/material-remixer-android/tree/develop/remixer_core/src/main/java/com/google/android/libraries/remixer/annotation) for more information.

For example:

```java
@RangeVariableMethod(minValue = 15, maxValue = 70, initialValue = 20)
public void setFontSize(Float fontSize) {
  titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
}
```

Additionally, you need to add `RemixerBinder.bind(this)` at the end of your activity's `onCreate(Bundle)`.

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  //...
  RemixerBinder.bind(this);
}
```

## Display the Remixer Fragment

You can configure the `RemixerFragment` in the `Activity`'s `onCreate(Bundle)` method, after the call to `RemixerBinder.bind(this)`. You have 3 (not mutually-exclusive) options:

1. [Attach the RemixerFragment to a button click](CONFIGURE_UI.md#attach-the-remixer-fragment-to-a-button), `RemixerFragment#attachToButton(FragmentActivity, Button)`
2. [Attach the RemixerFragment to a FAB](CONFIGURE_UI.md#attach-the-remixer-fragment-to-a-fab), `RemixerFragment#attachToFab(FragmentActivity, FloatingActionButton)`
3. [Attach the RemixerFragment to a multi-touch gesture](CONFIGURE_UI.md#attach-the-remixer-fragment-to-a-multi-touch-gesture), `RemixerFragment#attachToButton(FragmentActivity, Button)`
4. [Attach the RemixerFragment to a ShakeListener](CONFIGURE_UI.md#attach-the-remixer-fragment-to-a-shake), `RemixerFragment#attachToShake(FragmentActivity, double)`. *In order to conserve battery, you must call `attachToShake` from `onResume` and call `detachFromShake` from `onPause`*

Detailed examples can be found on the [Configure the UI](CONFIGURE_UI.md) page.


_You may not want to use the `RemixerFragment` in case you only want to use a FirebaseRemoteController. Adding a RemixerFragment does affect the UI you may be playing with, so it may be desirable not to use it and adjust values remotely instead._
