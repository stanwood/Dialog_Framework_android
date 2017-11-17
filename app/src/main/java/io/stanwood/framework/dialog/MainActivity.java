package io.stanwood.framework.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RatingService.with(this)
                .setLaunchTimes(5)
                .init();

        if (RatingService.shouldBeDisplayed()) {
            RatingDialog.newBuilder()
                    .setText1("Hi,\n ich bin Hannes, der Entwicker\n von ON AIR.")
                    .setText2("Kleine App-Entwicker wie wir leben von gutten Bewertungen im App-Store.")
                    .setText3("Wenn Ihnen unsere App gefallt dann bewertend Sie uns doch bitte.")
                    .setText4("Sternchen reichen - dauert nur 1 Minute.")
                    .setCancelText("Schließen")
                    .setOkText("App bewerten")
                    .setBannerUrl("https://media.istockphoto.com/photos/plitvice-lakes-picture-id500463760?s=2048x2048")
                    .setFaceUrl("https://lh5.googleusercontent.com/-_w2wo1s6SkI/AAAAAAAAAAI/AAAAAAAAhMU/s78iSxXwVZk/photo.jpg")
                    .build()
                    .show(getSupportFragmentManager(), "dialog");
        }
    }

}
