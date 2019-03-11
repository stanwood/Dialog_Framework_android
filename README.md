[![Release](https://jitpack.io/v/stanwood/Dialog_Framework_android.svg?style=flat-square)](https://jitpack.io/#stanwood/Dialog_Framework_android)

# stanwood Rating Dialog (Android)

This library contains a simple to use "please rate my app" dialog, which is shown at x-th time of app starts.

![Demo](indicator.gif)

## Import

The stanwood Rating Dialog is hosted on JitPack. Therefore you can simply import it by adding

```groovy
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
}
```

to your project's `build.gradle`.

Then add this to you app's `build.gradle`:

```groovy
dependencies {
    implementation 'com.github.stanwood:Dialog_Framework_android:<insert latest version here>'
    implementation 'com.github.stanwood.Core_Framework_android:framework-base:<insert latest version here>'
    implementation 'com.github.stanwood.Core_Framework_android:framework-ui:<insert latest version here>'
}
```

## Usage

Initialise the dialog within Application and set after how many app starts would you want to show the dialog.

```java
public class MyApp extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Inject
    RatingService ratingService;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder().application(this).build().inject(this);
        
        ratingService.setLaunchTimes(2);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
```

Show the dialog within Activity
```java
public class MainActivity extends AppCompatActivity {

    @Inject
    RatingService ratingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        setContentView(R.layout.activity_main);
        if (ratingService.shouldBeDisplayed()) {
            RatingDialog.builder()
                    .setParagraph1("Hi,\nI'm the developer of this app.")
                    .setParagraph2("Developers like me live on good ratings in Google Play.")
                    .setParagraph3("If you like the app, please rate it.")
                    .setParagraph4("It takes only 1 minute.")
                    .setCancelText("Cancel")
                    .setOkText("Ok")
                    .setBannerUrl(<add url to image here>)
                    .setFaceUrl(<<add url to image here>)
                    .build()
                    .show(getSupportFragmentManager(), "dialog");
        }
    }
```

For more information check out the sample app.

## Roadmap

- add more possibilities about when to show the dialog
