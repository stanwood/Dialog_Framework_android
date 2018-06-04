package io.stanwood.framework.demo.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.stanwood.framework.dialog.RatingService;

@Module
class AppModule {

    @Singleton
    @Provides
    RatingService provideRatingService(Application context) {
        return new RatingService(context);
    }

}
