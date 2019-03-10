package ch.mobpro.eventapp.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.LoaderManager.LoaderCallbacks
import android.arch.lifecycle.Observer
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.widget.Toast
import ch.mobpro.eventapp.R
import ch.mobpro.eventapp.base.BaseActivity
import ch.mobpro.eventapp.databinding.ActivityLoginBinding
import ch.mobpro.eventapp.di.util.ViewModelFactory
import ch.mobpro.eventapp.viewmodel.LoginViewModel
import ch.mobpro.eventapp.viewmodel.validation.EmailErrorEvent
import ch.mobpro.eventapp.viewmodel.validation.PasswordErrorEvent
import ch.mobpro.eventapp.viewmodel.validation.getError
import io.reactivex.internal.util.NotificationLite.getError

import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

    }

    private fun onLoginFormSuccess(it: Boolean?) {
        if (it == true) {
            val intent = Intent(this, MainActivity::class.java)
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
