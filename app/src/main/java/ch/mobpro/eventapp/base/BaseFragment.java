package ch.mobpro.eventapp.base;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import dagger.android.DaggerFragment;

public abstract class BaseFragment<T extends ViewModel> extends DaggerFragment {

    private T viewModel;

    /**
     *
     * @return view model instance
     */
    public abstract T getViewModel();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        viewModel = getViewModel();
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }

}
