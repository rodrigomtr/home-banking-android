package com.example.rodrigobrodrigues.accentureHomeBanking.login

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.Toast

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.home.MainActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.proving.PaymentProvingActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.util.NetworkChangeReceiver
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.User
import kotlinx.android.synthetic.main.login.*

/**
 *
 */
class LoginActivity : AbstractAsyncActivity(), NetworkChangeReceiver.OnNetworkChangeListener, LoginContract.View {

    private var loginPresenter: LoginContract.Presenter? = null

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.checkConnection()
        setContentView(R.layout.login)

        val sharedPreferences =  getSharedPreferences(
                "com.example.rodrigobrodrigues.accentureHomeBanking",
                Context.MODE_PRIVATE)

        this.loginPresenter = LoginPresenter(this, sharedPreferences)

        loadViews()
    }

    /**
     * Method that loads view components
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun loadViews() {

        loginPresenter!!.setRememberMe()
        sign_in.setOnClickListener{
            cleanErrors()
            val isNotification: Boolean = intent.getBooleanExtra("notification", false)
            loginPresenter!!.execute(remember_me.isChecked, isNotification, account.text.toString(), pw.text.toString())
        }
    }

    private fun displayResponse(response: String) {
        Toast.makeText(this, "Welcome $response", Toast.LENGTH_LONG).show()
    }

    private fun cleanErrors() {
        tv_pw_error.visibility = View.GONE
        tv_account_error.visibility = View.GONE
        error.visibility = View.GONE
        account.background.colorFilter = null
        pw.background.colorFilter = null
    }

    override fun displayError(errorContent: Int) {
        error.text = getString(errorContent)
        error.visibility = View.VISIBLE
    }

    override fun start(result: User) {
        if (intent.getStringExtra("context") != null) {
            startForNotification()
        } else {
            startForNoContext()
        }
    }

    override fun startForNotification() {
        val currentUser = App.instance?.user
        displayResponse(currentUser!!.client.firstName)

        val intent = Intent(applicationContext, PaymentProvingActivity::class.java)
        intent.putExtras(getIntent().extras!!)

        startActivity(intent)
    }

    override fun startForNoContext() {
        val currentUser = App.instance?.user
        displayResponse(currentUser!!.client.firstName)
        startActivity(Intent(applicationContext, MainActivity::class.java))
    }

    override fun setPasswordFieldError() {
        tv_pw_error.text = getString(R.string.error_invalid_password)
        tv_pw_error.visibility = View.VISIBLE
        setErrorET(pw)
    }

    override fun setUsernameFieldError() {
        tv_account_error.text = getString(R.string.error_invalid_account)
        tv_account_error.visibility = View.VISIBLE
        account.background.setColorFilter(ContextCompat.getColor(context, R.color.error),
                PorterDuff.Mode.SRC_ATOP)
    }

    override fun setUsernameField(savedAccount: String) {
        account.setText(savedAccount)
    }

    override fun setCheckBoxChecked() {
        remember_me.isChecked = true
    }

    override fun focusPassword() {
        pw.requestFocus()
    }
}
