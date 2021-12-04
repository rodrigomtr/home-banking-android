package com.example.rodrigobrodrigues.accentureHomeBanking.home

/**
 * @author Rodrigo Rodrigues on 22/03/2018.
 */

interface MainContract {
    interface View {

        fun startPayments()

        fun showMatrixError()

        fun showMatrixCreatedDialog()

        fun setError()

        fun showLoadingProgressDialog()

        fun dismissProgressDialog()
    }

    interface Presenter {

        fun checkMatrix()

        fun createMatrix()

        fun sendNotificationToken()
    }
}
