package com.example.rodrigobrodrigues.accentureHomeBanking.payments.socialSecurity

import android.os.Bundle
import android.view.View

import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentContract
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentView
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.MonthDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.PaymentTypeDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.RemunTypeDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.YearDialog
import kotlinx.android.synthetic.main.activity_sspayment.*
import kotlinx.android.synthetic.main.content_sspayment.*

class SSPaymentActivity
    : BasePaymentView(), SSPaymentContract.View, PaymentTypeDialog.OnSetPaymentTypeListener,
        RemunTypeDialog.OnSetRemunTypeListener, MonthDialog.OnSetMonthListener,
        YearDialog.OnSetYearListener, BasePaymentContract.View {

    private var presenter: SSPaymentContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sspayment)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadView()

        val sharedPreferences = context.getSharedPreferences("com.example.rodrigobrodrigues.accentureHomeBanking",
                        MODE_PRIVATE)

        presenter = SSPaymentPresenter(this, sharedPreferences)
    }

    private fun loadView() {
        tipo_pagamento.setOnClickListener{
            val ptDialog = PaymentTypeDialog()
            ptDialog.show(supportFragmentManager, "SSPAYMENT")
        }
        tipo_remuneracao.setOnClickListener{
            val rtDialog = RemunTypeDialog()
            rtDialog.show(supportFragmentManager, "SSPAYMENT")
        }
        btn_month.setOnClickListener{
            val mDialog = MonthDialog()
            mDialog.show(supportFragmentManager, "SSPAYMENT")
        }
        btn_year.setOnClickListener{
            val yDialog = YearDialog()
            yDialog.show(supportFragmentManager, "SSPAYMENT")
        }
        btn_pay.setOnClickListener{
            cleanErrors()
            presenter!!.setSSNumber(ssnumber.text.toString())
            presenter!!.setDaysOrHours(et_daysOrMonths.text.toString())
            presenter!!.validate()
        }

        btn_month.isEnabled = false

        setETTextChange(ssnumber, amount_entry_1)
    }

    private fun cleanErrors() {
        tv_ssNumber_error.visibility = View.GONE
        tv_daysOrMonths_error.visibility = View.GONE
        btn_year_error.visibility = View.GONE
        btn_month_error.visibility = View.GONE
        super.setDefaultET(ssnumber)
        super.setDefaultET(et_daysOrMonths)
        super.setDefaultET(amount_entry_1)
        super.setDefaultET(amount_entry_2)
    }

    override fun onSetYear(position: Int) {
        presenter!!.setPeriodYear(position)
        btn_month.isEnabled = true
    }

    override fun onSetMonth(position: Int) {
        presenter!!.setPeriodMonth(position)
    }

    override fun onSetRemunType(position: Int) {
        presenter!!.setRemunType(position)
        tipo_remuneracao.hint = resources.getStringArray(R.array.remun_type_array)[position]
    }

    override fun onSetPaymentType(position: Int) {
        presenter!!.setPaymentType(position)
    }

    override fun setBtnPeriodYearHint(periodYear: String) {
        btn_year.hint = periodYear
    }

    override fun setBtnPeriodMonthHint(position: Int) {
        btn_month.hint = resources.getStringArray(R.array.month_array)[position]
    }

    override fun setBtnRemunTypeBtnHint(position: Int) {
        tipo_remuneracao.hint = context.resources.getStringArray(R.array.remun_type_array)[position]
    }

    override fun enableRemunMonth() {
        amount.visibility = View.VISIBLE
        month_remun.visibility = View.VISIBLE
        et_daysOrMonths.visibility = View.GONE
        tv_daysOrMonths.visibility = View.GONE
    }

    override fun enableDays() {
        amount.visibility = View.GONE
        month_remun.visibility = View.GONE
        et_daysOrMonths.visibility = View.VISIBLE
        tv_daysOrMonths.visibility = View.VISIBLE
        tv_daysOrMonths.setText(R.string.days_number)
    }

    override fun enableHours() {
        amount.visibility = View.GONE
        month_remun.visibility = View.GONE
        et_daysOrMonths.visibility = View.VISIBLE
        tv_daysOrMonths.visibility = View.VISIBLE
        tv_daysOrMonths.setText(R.string.hours_number)
    }

    override fun setAmount(amount: Double?) {
        tv_amount.text = CurrencyHandler.displayCurrency(amount)
    }

    override fun periodMonthError() {
        btn_month_error.visibility = View.VISIBLE
        btn_month_error.text = getString(R.string.field_required)
    }

    override fun periodYearError() {
        btn_year_error.visibility = View.VISIBLE
        btn_year_error.text = getString(R.string.field_required)
    }

    override fun setHoursOrDaysError() {
        tv_daysOrMonths_error.visibility = View.VISIBLE
        tv_daysOrMonths_error.text = getString(R.string.field_required)
        super.setErrorET(et_daysOrMonths)
    }

    override fun ssNumberError(stringId: Int) {
        tv_ssNumber_error.visibility = View.VISIBLE
        tv_ssNumber_error.text = context.getString(stringId)
        super.setErrorET(ssnumber)
    }

    override fun setArguments(ss_entity: String, amount: Double?, accountId: Long, date: String, result: Int?) {
        super.getIntent().putExtra("entity", ss_entity)
        super.getIntent().putExtra("amount", amount)
        super.getIntent().putExtra("account", accountId)
        super.getIntent().putExtra("date", date)
        super.getIntent().putExtra("paymentId", result)
    }

    override fun setBtnPaymentType(position: Int) {
        tipo_pagamento.hint = context.resources.getStringArray(R.array.payment_type_array)[position]
    }

    override fun onSuccessfulAuth() {
        presenter!!.pay()
    }

    override fun getPaymentType(position: Int): String {
        return context.resources.getStringArray(R.array.payment_type_array)[position]
    }
    override fun getRemunType(position: Int): String {
        return context.resources.getStringArray(R.array.remun_type_array)[position]
    }

    override fun showConfirmationDialog(account: Long, ssEntity: String, ssNumber: String?, paymentType: Int, remunType: Int, daysOrHours: Int, time: String, amount: String?, date: String?) {
        val message = context.getString(R.string.account) + "\n" + account + "\n\n" +
                context.getString(R.string.ss_entity) + "\n" + ssEntity + "\n\n" +
                context.getString(R.string.ss_number) + "\n" + ssNumber + "\n\n" +
                context.getString(R.string.payment_type) + "\n" + getPaymentType(paymentType) + "\n\n" +
                context.getString(R.string.remun_type) + "\n" + getRemunType(remunType) +
                context.getString(daysOrHours) + ":" + "\n" + time + "\n\n" +
                context.getString(R.string.amount) + "\n" + amount + "\n\n" +
                context.getString(R.string.payment_date) + "\n" + date
        super.showConfirmationDialog(message)
    }

    override fun showConfirmationDialog(account: Long, ssEntity: String, ssNumber: String?, paymentType: Int, remunType: Int, amount: String?, date: String?) {
        val message = context.getString(R.string.account) + "\n" + account + "\n\n" +
                context.getString(R.string.ss_entity) + "\n" + ssEntity + "\n\n" +
                context.getString(R.string.ss_number) + "\n" + ssNumber + "\n\n" +
                context.getString(R.string.payment_type) + "\n" + getPaymentType(paymentType) + "\n\n" +
                context.getString(R.string.remun_type) + "\n" + getRemunType(remunType) +
                context.getString(R.string.amount) + "\n" + amount + "\n\n" +
                context.getString(R.string.payment_date) + "\n" + date
        super.showConfirmationDialog(message)
    }
}
