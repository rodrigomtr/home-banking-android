package com.example.rodrigobrodrigues.accentureHomeBanking.transactionAuth

import com.example.rodrigobrodrigues.accentureHomeBanking.App
import com.example.rodrigobrodrigues.accentureHomeBanking.transactionAuth.tasks.MatrixAuthTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

import java.util.Random

/**
 * @author Rodrigo Rodrigues on 16/02/2018.
 */

internal class SimpleTransactionAuthPresenter(view: TransactionAuthDialogFragment)
    : TransactionAuthContract.Presenter{

    private val view: TransactionAuthContract.View = view
    private var l1: Int = 0
    private var c1: Int = 0
    private var l2: Int = 0
    private var c2: Int = 0
    private var l3: Int = 0
    private var c3: Int = 0
    private var l4: Int = 0
    private var c4: Int = 0

    init {
        startMatrixAuth()
    }

    override fun startMatrixAuth() {
        view.setMatrixView()
        setRequest()
    }

    private fun setRequest() {
        val rand = Random()
        l1 = rand.nextInt(5)
        c1 = rand.nextInt(5)
        l2 = rand.nextInt(5)
        c2 = rand.nextInt(5)
        l3 = rand.nextInt(5)
        c3 = rand.nextInt(5)
        l4 = rand.nextInt(5)
        c4 = rand.nextInt(5)
        val req1 = (65 + l1).toChar() + c1.toString()
        val req2 = (65 + l2).toChar() + c2.toString()
        val req3 = (65 + l3).toChar() + c3.toString()
        val req4 = (65 + l4).toChar() + c4.toString()
        view.requestPassword(req1, req2, req3, req4)
    }

    override fun matrixAuth() {
        val matrixId = App.instance!!.user!!.matrix!!.id
        val password = view.password
        try {
            val request = (matrixId.toString() + "%" + l1 + ":" + c1 + ":" + password[0]
                    + "!" + l2 + ":" + c2 + ":" + password[1]
                    + "!" + l3 + ":" + c3 + ":" + password[2]
                    + "!" + l4 + ":" + c4 + ":" + password[3])

            launch(UI) {
                view.showLoadingProgressDialog()
                val authResult = MatrixAuthTask().auth(request)
                view.dismissProgressDialog()
                if (authResult) view.matrixSuccess() else view.matrixError()
            }
        } catch (e: StringIndexOutOfBoundsException) {
            view.matrixError()
        }
    }

    override fun onPositiveButtonClicked() {
        matrixAuth()
    }
}