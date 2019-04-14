package ch.mobpro.eventapp.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import ch.mobpro.eventapp.R
import ch.mobpro.eventapp.base.BaseActivity
import ch.mobpro.eventapp.databinding.ActivityLoginBinding
import ch.mobpro.eventapp.ui.LayoutActivityId
import ch.mobpro.eventapp.viewmodel.LoginViewModel
import ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent
import ch.mobpro.eventapp.viewmodel.validation.PasswordErrorEvent
import ch.mobpro.eventapp.viewmodel.validation.getError

import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {
    val TAG = LoginActivity::class.java.simpleName

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: LoginViewModel

    override fun layoutRes(): Int {
        return R.layout.activity_login
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
        dataBinding.viewModel = viewModel

        viewModel.emailErrorEvent.observe(this, Observer { onEmailEventError(it) })
        viewModel.passwordErrorEvent.observe(this, Observer { onPasswordEventError(it) })
        viewModel.loginSuccess.observe(this, Observer { onLoginFormSuccess(it) })
        viewModel.isLoading.observe(this, Observer { onLoading(it) })
        viewModel.layoutActivity.observe(this, Observer { navigateToActivity(it) })

    }

    private fun onLoading(isLoading: Boolean?) {
        if (isLoading == true) {
            findViewById<ProgressBar>(R.id.loginProgressBar).isActivated = true
            findViewById<ProgressBar>(R.id.loginProgressBar).visibility = VISIBLE
        } else {
            findViewById<ProgressBar>(R.id.loginProgressBar).isActivated = false
            findViewById<ProgressBar>(R.id.loginProgressBar).visibility = INVISIBLE
        }
    }

    private fun navigateToActivity(layoutActivityId: LayoutActivityId?) {
        when (layoutActivityId) {
            LayoutActivityId.REGISTER_ACTIVITY -> startActivity(Intent(this, RegisterActivity::class.java))
            else -> Log.w(TAG, "Got LayoutActivityId for which no navigation exists in layout")
        }
    }

    private fun onLoginFormSuccess(loginSuccess: Boolean?) {
        findViewById<ProgressBar>(R.id.loginProgressBar).isActivated = false
        findViewById<ProgressBar>(R.id.loginProgressBar).visibility = INVISIBLE
        if (loginSuccess == true) {
            val intent = Intent(this, EventListActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.wrong_credentials), Toast.LENGTH_LONG).show()
        }

    }

    private fun onEmailEventError(emailError: EmailErrorEvent?) {

        dataBinding.username.error = getError(this, emailError)
    }

    private fun onPasswordEventError(passwordError: PasswordErrorEvent?) {
        dataBinding.password.error = getError(this, passwordError)
    }

}
