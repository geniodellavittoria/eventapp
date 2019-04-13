package ch.mobpro.eventapp.di.module;

import ch.mobpro.eventapp.activity.LoginActivity;
import ch.mobpro.eventapp.activity.RegisterActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract LoginActivity contributeLoginActivity();

    @ContributesAndroidInjector(modules = {FragmentBuildersModule.class})
    abstract RegisterActivity contributeRegisterActivity();
}