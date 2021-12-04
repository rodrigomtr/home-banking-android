package com.example.rodrigobrodrigues.accentureHomeBanking.payments.tax

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentView
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.frequents.FreqPaymentsActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.textRecognizer.TextRecognizerActivity
import kotlinx.android.synthetic.main.content_tax_payment.*

/**
 * @author Rodrigo Rodrigues 26/02/2018
 */
class TaxPaymentActivity
    : BasePaymentView(), TaxPaymentContract.View {

    private val freqRequestCode = 101
    private val detectorRequestCode = 110

    private var presenter: TaxPaymentContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tax_payment)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        loadView()

        val sharedPreferences = context.getSharedPreferences("com.example.rodrigobrodrigues.accentureHomeBanking",
                        MODE_PRIVATE)

        this.presenter = TaxPaymentPresenter(this, sharedPreferences)
    }

    private fun loadView() {

        setETTextChange(ref_entry_1, ref_entry_2)
        setETTextChange(ref_entry_2, ref_entry_3)
        setETTextChange(ref_entry_3, ref_entry_4)
        setETTextChange(ref_entry_4, ref_entry_5)
        setETTextChange(ref_entry_5, amount_entry_2)
        setETTextChange(amount_entry_2, amount_entry_1)

        btn_cam.setOnClickListener{
            val intent = Intent(this@TaxPaymentActivity, TextRecognizerActivity::class.java)
            intent.putExtra("PARENT", "IMP")
            startActivityForResult(intent, detectorRequestCode)
        }
        btn_frequents.setOnClickListener{
            val intentFreq = Intent(this@TaxPaymentActivity, FreqPaymentsActivity::class.java)
            intentFreq.putExtra("PARENT", "IMP")
            startActivityForResult(intentFreq, freqRequestCode)
        }
        btn_pay.setOnClickListener{
            cleanErrorView()
            val ref = ref_entry_1.text.toString() + ref_entry_2.text.toString() +
                    ref_entry_3.text.toString() + ref_entry_4.text.toString() + ref_entry_5.text.toString()
            val amount = amount_entry_1.text.toString() + "." + amount_entry_2.text.toString()

            presenter!!.verifyForm(ref, amount)
        }

        loadPreDefined(intent)
    }

    private fun cleanErrorView() {
        tv_error_amount.visibility = View.GONE
        tv_error_ref.visibility = View.GONE
        tv_error.visibility = View.INVISIBLE
        super.setDefaultET(ref_entry_1)
        super.setDefaultET(ref_entry_2)
        super.setDefaultET(ref_entry_3)
        super.setDefaultET(ref_entry_4)
        super.setDefaultET(ref_entry_5)
        super.setDefaultET(amount_entry_1)
        super.setDefaultET(amount_entry_2)
    }

    override fun showRefError() {
        tv_error_ref.visibility = View.VISIBLE
        tv_error_ref.text = getString(R.string.error_ref)
        super.setErrorET(ref_entry_1)
        super.setErrorET(ref_entry_2)
        super.setErrorET(ref_entry_3)
        super.setErrorET(ref_entry_4)
        super.setErrorET(ref_entry_5)
    }

    override fun showAmountError() {
        tv_error_amount.visibility = View.VISIBLE
        tv_error_amount.text = getString(R.string.error_amount)
        super.setErrorET(amount_entry_1)
        super.setErrorET(amount_entry_2)
    }

    override fun showConfirmationDialog(account: Long, ref: String?, amount: String?, date: String?) {
        val message = context.getString(R.string.account) + "\n" + account + "\n\n" +
                context.getString(R.string.ref) + "\n" + ref + "\n\n" +
                context.getString(R.string.amount) + "\n" + amount + "\n\n"+
                context.getString(R.string.payment_date) + "\n" + date
        super.showConfirmationDialog(message)
    }

    override fun showFieldsError() {
        tv_error.visibility = View.VISIBLE
        tv_error.setText(R.string.error_payment_fields)
    }

    override fun setArguments(entity: String, ref: String, amount: Double?, accountId: Long, date: String, result: Int?) {
        super.getIntent().putExtra("entity", entity)
        super.getIntent().putExtra("ref", ref)
        super.getIntent().putExtra("amount", amount)
        super.getIntent().putExtra("account", accountId)
        super.getIntent().putExtra("date", date)
        super.getIntent().putExtra("paymentId", result)
    }

    override fun onSuccessfulAuth() {
        presenter!!.pay()
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

    private fun loadPreDefined(data: Intent?) {
        val ref = data?.getStringExtra("ref")
        val amount = data?.getStringExtra("amount")

        if(ref != null && amount != null) {
            ref_entry_1.setText(ref.substring(0, 3))
            ref_entry_2.setText(ref.substring(3, 6))
            ref_entry_3.setText(ref.substring(6, 9))
            ref_entry_4.setText(ref.substring(9, 12))
            ref_entry_5.setText(ref.substring(12, 15))

            amount_entry_1.setText(amount.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0])
            amount_entry_2.setText(amount.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1])
        }
    }
}
