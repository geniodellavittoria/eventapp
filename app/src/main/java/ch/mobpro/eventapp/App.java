package ch.mobpro.eventapp;

import android.app.Application;
import ch.mobpro.eventapp.di.component.ApplicationComponent;
import ch.mobpro.eventapp.di.component.DaggerApplicationComponent;

public class App extends Application {

    private static ApplicationComponent component;

    public static ApplicationComponent getComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        component = buildComponent();
    }

    private ApplicationComponent buildComponent() {
        return DaggerApplicationComponent.builder()
                .build();
    }
}
