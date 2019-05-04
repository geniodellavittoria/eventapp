package ch.mobpro.eventapp.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ch.mobpro.eventapp.di.util.ViewModelFactory
import ch.mobpro.eventapp.di.util.ViewModelKey
import ch.mobpro.eventapp.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    internal abstract fun bindRegisterViewModel(registerViewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    internal abstract fun bindEventListViewModel(eventListViewModel: EventListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CreateEventViewModel::class)
    internal abstract fun bindCreateEventViewModel(createEventViewModel: CreateEventViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(EditEventViewModel::class)
    internal abstract fun bindEditEventViewModel(editEventViewModel: EditEventViewModel): ViewModel

    /*

    @Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    internal abstract fun bindListViewModel(listViewModel: EventListViewModel): ViewModel
    @Provides
    //@Named("HomeFragment")
    fun provideViewModel(viewModel: ViewModel): ViewModelProvider.Factory {
        ViewModelProvider.Factory
        return ViewModelProviderFactory(homeFragmentViewModel)
    }
    */
}