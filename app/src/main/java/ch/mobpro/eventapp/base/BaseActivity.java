package ch.mobpro.eventapp.base;

import android.arch.lifecycle.ViewModel;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import butterknife.ButterKnife;
import ch.mobpro.eventapp.di.util.Injectable;
import dagger.android.AndroidInjection;
import dagger.android.support.DaggerAppCompatActivity;

public abstract class BaseActivity<B extends ViewDataBinding> extends DaggerAppCompatActivity implements Injectable {

    protected B dataBinding;

    @LayoutRes
    protected abstract int layoutRes();

    protected void bindView(int layoutId) {
        dataBinding = DataBindingUtil.setContentView(this, layoutId);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        bindView(layoutRes());
        //setContentView(layoutRes());
        ButterKnife.bind(this);
    }
}
