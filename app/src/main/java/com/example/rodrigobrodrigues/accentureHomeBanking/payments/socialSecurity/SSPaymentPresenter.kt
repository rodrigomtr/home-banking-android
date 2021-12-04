package com.example.rodrigobrodrigues.accentureHomeBanking.payments.socialSecurity

import android.content.SharedPreferences
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentPresenter
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tasks.SSPaymentTask
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * @author Rodrigo Rodrigues on 26/02/2018.
 */

class SSPaymentPresenter internal constructor(private val view: SSPaymentContract.View, sharedPreferences: SharedPreferences)
    : BasePaymentPresenter(view, sharedPreferences), SSPaymentContract.Presenter{

    private var periodYear: Int = 20
    private var periodMonth: Int = 20
    private var remunType: Int = 0
    private var paymentType: Int = 0
    private var ssNumber: String? = null
    private var time: String? = null
    private var amount: Double? = null
    private val ssEntity = "21056"

    private val isFormValid: Boolean
        get() {
            if (periodYear == 20) {
                view.periodYearError()
                return false
            }
            if (periodMonth == 20) {
                view.periodMonthError()
                return false
            }
            if (remunType != 0 && time!!.isEmpty()) {
                view.setHoursOrDaysError()
                return false
            }
            if (ssNumber!!.isEmpty()) {
                view.ssNumberError(R.string.field_required)
                return false
            }
            if (ssNumber!!.length != 11) {
                view.ssNumberError(R.string.digit_number_error)
                return false
            }
            return true
        }

    init {
        setPaymentType(0)
        setRemunType(0)
    }

    override fun setPeriodYear(position: Int) {
        this.periodYear = 2017 + position
        view.setBtnPeriodYearHint(periodYear.toString() + "")
    }

    override fun setPeriodMonth(position: Int) {
        periodMonth = position
        view.setBtnPeriodMonthHint(periodMonth)
        calculateAmount()
    }

    override fun setRemunType(position: Int) {
        remunType = position
        view.setBtnRemunTypeBtnHint(remunType)
        when (position) {
            0 -> {
                view.enableRemunMonth()
            }
            1 -> {
                view.enableDays()
            }
            2 -> {
                view.enableHours()
            }
        }
    }

    override fun setPaymentType(position: Int) {
        paymentType = position
        view.setBtnPaymentType(position)
    }

    override fun pay() {
        val data = (super.account.id.toString() + ":" + ssEntity + ":" + ssNumber
                + ":" + amount + ":" + super.year + "-" + (super.month + 1)
                + "-" + super.day + ":" + periodMonth
                + ":" + periodYear + ":" + view.getPaymentType(paymentType)
                + ":" + view.getRemunType(remunType) + ":" + time)
        serverRequest(data)
    }

    private fun serverRequest(request: String) {
        launch(UI) {
            view.showLoadingProgressDialog()
            val serverResult = SSPaymentTask().pay(request)
            view.dismissProgressDialog()
            if (serverResult != -1) {
                view.setArguments(ssEntity, amount, super.account.id,
                        java.sql.Date.valueOf(
                                super.year.toString()
                                        + "-" + (super.month + 1)
                                        + "-" + super.day).toString()
                        , serverResult)
                view.showSuccessDialog()
            } else {
                view.showFailureDialog()
            }
        }
    }

    override fun setDaysOrHours(string: String) {
        this.time = string
    }

    override fun setSSNumber(ssNumber: String) {
        this.ssNumber = ssNumber
    }

    override fun validate() {
        if (isFormValid) {
            showConfirmation()
        }
    }

    private fun showConfirmation() {
        if (!time!!.isEmpty()) {
            val daysOrHours: Int =
                    if (remunType == 1) R.string.days_number
                    else R.string.hours_number

            view.showConfirmationDialog(super.account.id, ssEntity, ssNumber, paymentType,
                    remunType, daysOrHours, time!!, CurrencyHandler.displayCurrency(amount), date )
        } else {
            view.showConfirmationDialog(super.account.id, ssEntity, ssNumber, paymentType,
                    remunType, CurrencyHandler.displayCurrency(amount), date)
        }
    }

    private fun calculateAmount() {
        when (remunType) {
            0 -> this.amount = 428.90 * 0.2375
            1 -> this.amount = 14.30 * 0.2375
            2 -> this.amount = 2.47 * 0.2375
        }
        view.setAmount(amount)
    }
}
