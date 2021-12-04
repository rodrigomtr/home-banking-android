package com.example.rodrigobrodrigues.accentureHomeBanking.login

import com.example.rodrigobrodrigues.accentureHomeBanking.models.User

/**
 * Interface that contains two inner interfaces
 * needed to follow mvp design pattern
 */
interface LoginContract {

    /**
     * Interface that contain the methods
     * that are used by the Presenter
     * and implemented by the View
     */
    interface View {

        /**
         * Method that shows loading dialog
         * while login process is running
         */
        fun showLoadingProgressDialog()

        /**
         * Method that dismiss loading dialog
         *
         */
        fun dismissProgressDialog()

        /**
         * Method that returns string from app resources
         * @param id string id (R.string.string_name)
         * @return the string from string resources
         */
        fun getString(id: Int): String

        /**
         * Method that set error message
         * and shows the same
         * @param errorContent message
         */
        fun displayError(errorContent: Int)

        /**
         * Starts new activity
         * @param result user object obtained from login
         */
        fun start(result: User)

        /**
         * Shows to user an error when password
         * requirements are not respected
         */
        fun setPasswordFieldError()

        /**
         * Shows to user an error when username
         * requirements are not respected
         */
        fun setUsernameFieldError()

        /**
         * When username is saved (remember-me)
         * the username field is filled by this method
         * @param savedAccount the value of the field
         */
        fun setUsernameField(savedAccount: String)

        /**
         * Method that turns the check box to checked
         */
        fun setCheckBoxChecked()

        /**
         * Method that focus password field when the
         * contract number field is already filled
         */
        fun focusPassword()

        fun startForNoContext()

        fun startForNotification()
    }

    /**
     * Interface that contain the methods
     * that are used by the View
     * and implemented by the Presenter
     */
    interface Presenter {

        /**
         * Method that execute login process
         */
        fun execute(rememberMe: Boolean, isNotification: Boolean, username: String, password: String)

        /**
         * Method that fill the username field
         */
        fun setRememberMe()
    }
}
