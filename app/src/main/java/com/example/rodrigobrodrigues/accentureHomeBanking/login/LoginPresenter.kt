package com.example.rodrigobrodrigues.accentureHomeBanking.login

import android.content.SharedPreferences

import com.example.rodrigobrodrigues.accentureHomeBanking.login.tasks.AuthenticationTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class LoginPresenter (private val view: LoginContract.View, private val sharedPreferences: SharedPreferences)
    : LoginContract.Presenter{

    private var isNotification: Boolean = false

    override fun execute(rememberMe:Boolean, isNotification: Boolean, username: String, password: String) {
        this.isNotification = isNotification
        if (handlePassword(password) && handleUsername(username)) {
            if (rememberMe) sharedPreferences.edit().putString("rememberMe", username).apply()
            launch(UI) {
                view.showLoadingProgressDialog()
                val request = AuthenticationTask().login(username, password)
                view.dismissProgressDialog()
                if (request == -1) setStartMode() else view.displayError(request)
            }
        }
    }

    private fun handlePassword(password: String): Boolean {
        return if (password.length != 6) {
            view.setPasswordFieldError()
            false
        } else true
    }

    private fun handleUsername(username: String): Boolean {
        return if (username.isEmpty()) {
            view.setUsernameFieldError()
            false
        } else true
    }

    override fun setRememberMe() {
        val rememberMe = sharedPreferences.getString("rememberMe", null)
        if (rememberMe?.isEmpty() == false) {
            view.setUsernameField(rememberMe)
            view.setCheckBoxChecked()
            view.focusPassword()
        }
    }

    private fun setStartMode() {
        if (isNotification) {
            view.startForNotification()

        } else {
            view.startForNoContext()
        }
    }
}