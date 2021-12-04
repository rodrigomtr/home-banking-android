package com.example.rodrigobrodrigues.accentureHomeBanking.payments.socialSecurity

import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentContract

/**
 * @author Rodrigo Rodrigues on 26/02/2018.
 */

internal interface SSPaymentContract {
    interface View : BasePaymentContract.View {

        fun setBtnPeriodYearHint(periodYear: String)

        fun setBtnPeriodMonthHint(position: Int)

        fun setBtnPaymentType(position: Int)

        fun setBtnRemunTypeBtnHint(position: Int)

        fun showLoadingProgressDialog()

        fun dismissProgressDialog()

        fun showSuccessDialog()

        fun showFailureDialog()

        fun enableRemunMonth()

        fun enableDays()

        fun enableHours()

        fun setAmount(amount: Double?)

        fun periodMonthError()

        fun periodYearError()

        fun setHoursOrDaysError()

        fun ssNumberError(stringId: Int)

        fun showConfirmationDialog(account: Long, ssEntity: String, ssNumber: String?, paymentType: Int,
                                   remunType: Int, daysOrHours: Int, time: String,
                                   amount: String?, date: String?)

        fun showConfirmationDialog(account: Long, ssEntity: String, ssNumber: String?, paymentType: Int,
                                   remunType: Int, amount: String?, date: String?)


        fun setArguments(ss_entity: String, amount: Double?, accountId: Long, date: String, result: Int?)

        fun getPaymentType(position: Int): String

        fun getRemunType(position: Int): String

    }

    interface Presenter : BasePaymentContract.Presenter {

        fun setPeriodYear(position: Int)

        fun setPeriodMonth(position: Int)

        fun setRemunType(position: Int)

        fun setPaymentType(position: Int)

        fun pay()

        fun setDaysOrHours(string: String)

        fun setSSNumber(ssNumber: String)

        fun validate()
    }
}
