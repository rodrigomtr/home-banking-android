package com.example.rodrigobrodrigues.accentureHomeBanking.payments.service

import android.animation.LayoutTransition
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.RadioGroup

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentView
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.OperatorListDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.PeriodSpinnerFragment
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.frequents.FreqPaymentsActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.textRecognizer.TextRecognizerActivity
import kotlinx.android.synthetic.main.activity_serv_payment.*
import kotlinx.android.synthetic.main.content_serv_payment.*

class ServicePaymentActivity : BasePaymentView(), ServicePaymentContract.View, PeriodSpinnerFragment.OnSetPeriodListener, RadioGroup.OnCheckedChangeListener, OperatorListDialog.OnSetOperatorListener {

    private val freqRequestCode = 100
    private val detectorRequestCode = 110
    private var presenter: ServicePaymentContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_serv_payment)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadViews()
        val sharedPreferences = context
                .getSharedPreferences("com.example.rodrigobrodrigues.accentureHomeBanking",
                        MODE_PRIVATE)
        this.presenter = ServicePaymentPresenter(this, sharedPreferences)
    }

    private fun loadPreDefined(intent: Intent?) {
        val entity = intent?.getStringExtra("entity")
        val ref = intent?.getStringExtra("ref")
        val amount = intent?.getStringExtra("amount")

        if (entity != null && ref != null && amount != null) {
            entity_entry.setText(entity)
            ref_entry_1.setText(ref.substring(0, 3))
            ref_entry_2.setText(ref.substring(3, 6))
            ref_entry_3.setText(ref.substring(6, 9))
            amount_entry_1.setText(amount.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            amount_entry_2.setText(amount.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
        }
    }

    private fun loadViews() {

        setETTextChange(entity_entry, ref_entry_1)
        setETTextChange(ref_entry_1, ref_entry_2)
        setETTextChange(ref_entry_2, ref_entry_3)
        setETTextChange(ref_entry_3, amount_entry_2)
        setETTextChange(amount_entry_2, amount_entry_1)

        btn_period.hint = resources.getStringArray(R.array.period_array)[0]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            (pre_defined_layout as ViewGroup).layoutTransition
                    .enableTransitionType(LayoutTransition.CHANGING)
        }

        radio_group.setOnCheckedChangeListener(this)

        btn_period.setOnClickListener{super.showPeriodDialog()}
        btn_operator.setOnClickListener{handleServiceOperator()}
        btn_frequents.setOnClickListener{
            val intentFreq = Intent(this@ServicePaymentActivity, FreqPaymentsActivity::class.java)
            intentFreq.putExtra("PARENT", "SERVICE")
            startActivityForResult(intentFreq, freqRequestCode)}
        btn_cam.setOnClickListener{
            val intent = Intent(this@ServicePaymentActivity, TextRecognizerActivity::class.java)
            intent.putExtra("PARENT", "SERVICE")
            startActivityForResult(intent, detectorRequestCode)}
        service_type.setOnClickListener{presenter!!.handleServiceType()}
        switch_service.setOnClickListener{presenter!!.handleServiceType()}
        btn_pay.setOnClickListener{
            cleanErrorViews()
            startPayment()
        }

        switch_service.isChecked = false

        loadPreDefined(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == freqRequestCode && resultCode == Activity.RESULT_OK) {
            loadPreDefined(data)
        }
        else if (requestCode == detectorRequestCode && resultCode == Activity.RESULT_OK) {
            loadPreDefined(data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun cleanErrorViews() {
        entity_error.visibility = View.GONE
        ref_error.visibility = View.GONE
        amount_error.visibility = View.GONE
        super.setDefaultET(entity_entry)
        super.setDefaultET(ref_entry_1)
        super.setDefaultET(ref_entry_2)
        super.setDefaultET(ref_entry_3)
        super.setDefaultET(amount_entry_1)
        super.setDefaultET(amount_entry_2)
    }

    private fun handleServiceOperator() {
        val radioButtonID = radio_group.checkedRadioButtonId
        val radioButton = radio_group.findViewById<View>(radioButtonID)
        val idx = radio_group.indexOfChild(radioButton)
        Log.d("RADIO", "-$idx")
        super.showOperatorDialog(idx)
    }

    override fun hidePredefinedEntity() {
        pat_tv.visibility = View.VISIBLE
        entity_entry.visibility = View.VISIBLE
        pre_defined_layout.visibility = View.GONE
        switch_service.isChecked = false
        entity_entry.requestFocus()    }

    override fun showPredefinedEntity() {
        radio_group.check(R.id.radio_tele)
        pat_tv.visibility = View.GONE
        entity_entry.visibility = View.GONE
        pre_defined_layout.visibility = View.VISIBLE
        switch_service.isChecked = true
        ref_entry_1.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(entity_entry.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    private fun startPayment() {
        presenter!!.validatePayment(entity_entry, ref_entry_1, ref_entry_2, ref_entry_3, amount_entry_1, amount_entry_2)
    }

    override fun showFieldsError() {
        tv_error.visibility = View.VISIBLE
        tv_error.setText(R.string.error_payment_fields)
    }

    override fun showEntityError() {
        entity_error.visibility = View.VISIBLE
        entity_error.text = getString(R.string.error_entity)
        super.setErrorET(entity_entry)
    }

    override fun showRefError() {
        ref_error.visibility = View.VISIBLE
        ref_error.text = getString(R.string.error_ref)
        super.setErrorET(ref_entry_1)
        super.setErrorET(ref_entry_2)
        super.setErrorET(ref_entry_3)
    }

    override fun showAmountError() {
        amount_error.visibility = View.VISIBLE
        amount_error.text = getString(R.string.error_amount)
        super.setErrorET(amount_entry_1)
        super.setErrorET(amount_entry_2)
    }

    override fun setArguments(entity: String, ref: String, amount: Double, accountId: Long, date: String, result: Int?) {
        super.getIntent().putExtra("entity", entity)
        super.getIntent().putExtra("ref", ref)
        super.getIntent().putExtra("amount", amount)
        super.getIntent().putExtra("account", accountId)
        super.getIntent().putExtra("date", date)
        super.getIntent().putExtra("paymentId", result)
    }

    override fun onSetPeriod(position: Int) {
        presenter!!.period = position
        btn_period.hint = resources.getStringArray(R.array.period_array)[position]
    }

    override fun onSuccessfulAuth() {
        presenter!!.pay()
    }

    override fun onCheckedChanged(radioGroup: RadioGroup, i: Int) {
        Log.d("ONCHECKED", "-$i")
        when (i) {
            R.id.radio_internet -> {
                presenter!!.setServiceType(0)
                btn_operator.hint = resources.getStringArray(R.array.internet_array)[0]
            }
            R.id.radio_tele -> {
                presenter!!.setServiceType(1)
                btn_operator.hint = resources.getStringArray(R.array.Tele_array)[0]
            }
            R.id.radio_trans -> {
                presenter!!.setServiceType(2)
                btn_operator.hint = resources.getStringArray(R.array.transp_array)[0]
            }
            else -> {
            }
        }
    }

    override fun onSetOperator(position: Int) {
        btn_operator.hint = context.resources.getStringArray(presenter!!.serviceTypeId(position))[position]
    }

    override fun showConfirmationDialog(account: Long, entity: String?, ref: String?, amount: String?, period: Int, date: String?) {
        val message = context.getString(R.string.account) + "\n" + account + "\n\n" +
                context.getString(R.string.entity) + "\n" + entity + "\n\n" +
                context.getString(R.string.ref) + "\n" + ref + "\n\n" +
                context.getString(R.string.amount) + "\n" + amount + "\n\n" +
                context.getString(R.string.period) + "\n"+
                context.resources.getStringArray(R.array.period_array)[period] + "\n\n" +
                context.getString(R.string.payment_date) + "\n" + date
        super.showConfirmationDialog(message)
    }
}