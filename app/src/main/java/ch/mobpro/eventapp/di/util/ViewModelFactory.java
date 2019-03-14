package ch.mobpro.eventapp.di.util;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels;

    @Inject
    public ViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> viewModels) {
        this.viewModels = viewModels;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends ViewModel> creator = viewModels.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry : viewModels.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass + ". Add it to ViewModelModule");
        }
        try {
            return (T) creator.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}