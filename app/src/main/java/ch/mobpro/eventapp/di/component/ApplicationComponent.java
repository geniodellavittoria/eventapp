package ch.mobpro.eventapp.di.component;

import android.app.Application;
import ch.mobpro.eventapp.di.module.ActivityModule;
import ch.mobpro.eventapp.di.module.ApiModule;
import ch.mobpro.eventapp.di.module.ContextModule;
import ch.mobpro.eventapp.di.module.ViewModelModule;
import ch.mobpro.eventapp.service.AuthInterceptor;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

import javax.inject.Singleton;


@Singleton
@Component(modules = {ContextModule.class, ApiModule.class, ActivityModule.class, AndroidInjectionModule.class, ViewModelModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    void inject(Application application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        ApplicationComponent build();
    }
}
