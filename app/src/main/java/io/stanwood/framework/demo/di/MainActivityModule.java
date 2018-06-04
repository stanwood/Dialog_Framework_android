package io.stanwood.framework.demo.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import io.stanwood.framework.demo.MainActivity;

@Module
public abstract class MainActivityModule {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();
}

