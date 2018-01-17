package io.stanwood.framework.demo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.stanwood.framework.dialog.RatingDialog;
import io.stanwood.framework.dialog.RatingService;

public class MainActivity extends AppCompatActivity {

    @Inject
    RatingService ratingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);

        setContentView(R.layout.activity_main);
        if (ratingService.shouldBeDisplayed()) {
            RatingDialog dialog = RatingDialog.builder()
                    .setParagraph1("Hi,\nI'm the developer of this app.")
                    .setParagraph2("Developers like me live on good ratings in Google Play.")
                    .setParagraph3("If you like the app, please rate it.")
                    .setParagraph4("It takes only 1 minute.")
                    .setCancelText("Cancel")
                    .setOkText("Ok")
                    .setBannerUrl("https://media.istockphoto.com/photos/plitvice-lakes-picture-id500463760?s=2048x2048")
                    .setFaceUrl("https://lh5.googleusercontent.com/-_w2wo1s6SkI/AAAAAAAAAAI/AAAAAAAAhMU/s78iSxXwVZk/photo.jpg")
                    .build();
            //dialog.setAnalytics(add analytics instance here);
            dialog.show(getSupportFragmentManager(), "dialog");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
