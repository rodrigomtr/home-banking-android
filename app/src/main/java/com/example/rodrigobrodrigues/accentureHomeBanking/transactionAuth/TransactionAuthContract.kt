package com.example.rodrigobrodrigues.accentureHomeBanking.transactionAuth

/**
 * @author Rodrigo Rodrigues on 05/02/2018.
 */

internal interface TransactionAuthContract {
    interface View {

        val password: String

        val checkFPPermission: Boolean

        fun setFingerPrintView()

        fun setMatrixView()

        fun requestPassword(req1: String, req2: String, req3: String, req4: String)

        fun fingerPrintSuccess()

        fun setFingerPrintError()

        fun matrixSuccess()

        fun showLoadingProgressDialog()

        fun dismissProgressDialog()

        fun matrixError()
    }

    interface Presenter {

        fun matrixAuth()

        fun onPositiveButtonClicked()

        fun startMatrixAuth()
    }
}
