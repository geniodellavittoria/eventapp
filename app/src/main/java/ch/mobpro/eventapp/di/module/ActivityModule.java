package ch.mobpro.eventapp.di.module;

import ch.mobpro.eventapp.activity.LoginActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract LoginActivity contributeLoginActivity();
}