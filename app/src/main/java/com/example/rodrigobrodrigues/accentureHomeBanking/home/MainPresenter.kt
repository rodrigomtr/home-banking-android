package com.example.rodrigobrodrigues.accentureHomeBanking.home

import android.content.SharedPreferences
import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.CheckMatrixTask
import com.example.rodrigobrodrigues.accentureHomeBanking.home.tasks.CreateMatrixTask
import com.example.rodrigobrodrigues.accentureHomeBanking.home.tasks.SetNotificationTokenTask
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.UserTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * @author Rodrigo Rodrigues on 22/03/2018.
 */

class MainPresenter (private val view: MainContract.View, private val sharedPreferences: SharedPreferences)
    : MainContract.Presenter {

    override fun checkMatrix() {
        launch(UI) {
            view.showLoadingProgressDialog()
            val hasMatrix= CheckMatrixTask().hasMatrix()
            view.dismissProgressDialog()
            if(hasMatrix) view.startPayments() else view.showMatrixError()
        }
    }

    override fun createMatrix() {
        launch(UI) {
            view.showLoadingProgressDialog()
            if(CreateMatrixTask(App.instance!!.user!!.id).result()) {
                view.showMatrixCreatedDialog()
                UserTask().updateUser(App.instance!!.user!!.id);
            } else view.setError()

            view.dismissProgressDialog()
        }
    }

    override fun sendNotificationToken() {
        if (sharedPreferences.getBoolean("isRefreshed", false)) {
            val refreshedToken = sharedPreferences.getString("notificationToken", null)
            SetNotificationTokenTask().save(App.instance?.user?.id.toString() + ":-:" + refreshedToken)
            sharedPreferences.edit().putBoolean("isRefreshed", false).apply()
        }
    }
}