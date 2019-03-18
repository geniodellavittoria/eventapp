package ch.mobpro.eventapp.activity

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import ch.mobpro.eventapp.R
import ch.mobpro.eventapp.base.BaseActivity
import ch.mobpro.eventapp.databinding.ActivityRegisterBinding
import ch.mobpro.eventapp.viewmodel.RegisterViewModel
import javax.inject.Inject

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {
    val TAG = RegisterActivity::class.java.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: RegisterViewModel

    override fun layoutRes(): Int {
        return R.layout.activity_register
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)
        dataBinding.viewModel = viewModel
    }
}
