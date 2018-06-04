package io.stanwood.framework.demo;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;

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
            RatingDialog.builder()
                    .addParagraph(Html.fromHtml("<font color='blue'>Hi,</font>"))
                    .addParagraph("ich bin Hannes, der Entwicker von On Air")
                    .addParagraph("Kleine App-Entwicker wie wir leben von guten Bewertungen im App-Store.")
                    .addParagraph("Wenn Ihnen unsere App gefällt, dann bewerten Sie uns doch bitte.")
                    .addParagraph(Html.fromHtml("<font color='red'>Sternchen reichen - dauert nur 1 Minute</font>"))
                    .setCancelText("Cancel")
                    .setOkText("Ok")
                    .setBannerUrl("https://lh5.googleusercontent.com/-Lc9l-g5gkUM/AAAAAAAAAAI/AAAAAAAAAAg/NCA9hVKHbp4/photo.jpg")
                    .setFaceUrl("https://lh5.googleusercontent.com/-_w2wo1s6SkI/AAAAAAAAAAI/AAAAAAAAhMU/s78iSxXwVZk/photo.jpg")
                    //.setAnalyticsTracker(add analytics instance here)
                    .preloadAndShow(this);
        }
    }
}
