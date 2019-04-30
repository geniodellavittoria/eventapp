package ch.mobpro.eventapp.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
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

        viewModel.registrationSuccess.observe(this, Observer { isSuccess -> onRegistrationSuccess(isSuccess) })
        viewModel.isLoading.observe(this, Observer { onLoading(it) })
    }

    private fun onLoading(isLoading: Boolean?) {
        if (isLoading == true) {
            findViewById<ProgressBar>(R.id.registerProgressBar).isActivated = true
            findViewById<ProgressBar>(R.id.registerProgressBar).visibility = View.VISIBLE
        } else {
            findViewById<ProgressBar>(R.id.registerProgressBar).isActivated = false
            findViewById<ProgressBar>(R.id.registerProgressBar).visibility = View.INVISIBLE
        }
    }

    private fun onRegistrationSuccess(isSuccess: Boolean?) {
        if (isSuccess == null || !isSuccess) {
            Toast.makeText(this, "Could not register user", Toast.LENGTH_LONG).show()
        } else {
            Intent(this, LoginActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .also {
                    startActivity(it)
                }
        }
    }
}
