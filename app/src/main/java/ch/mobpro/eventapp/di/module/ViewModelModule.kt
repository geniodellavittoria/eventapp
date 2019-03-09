package ch.mobpro.eventapp.di.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import ch.mobpro.eventapp.di.util.ViewModelFactory
import ch.mobpro.eventapp.di.util.ViewModelKey
import ch.mobpro.eventapp.viewmodel.EventListViewModel
import ch.mobpro.eventapp.viewmodel.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {

    /*@Binds
    @IntoMap
    @ViewModelKey(EventListViewModel::class)
    internal abstract fun bindListViewModel(listViewModel: EventListViewModel): ViewModel
*/

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindListViewModel(listViewModel: LoginViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}