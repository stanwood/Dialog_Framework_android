/*
 * Copyright (c) 2018 stanwood Gmbh
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.framework.demo;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
