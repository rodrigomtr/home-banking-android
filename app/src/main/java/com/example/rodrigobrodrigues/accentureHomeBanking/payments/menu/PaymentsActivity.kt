package com.example.rodrigobrodrigues.accentureHomeBanking.payments.menu


import android.content.Intent
import android.os.Bundle
import android.widget.Toast

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.tax.TaxPaymentActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.scheduled.ScheduledPaymentsActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.socialSecurity.SSPaymentActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.util.QRCodeValidator
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.qrCode.QRPaymentActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.service.ServicePaymentActivity
import kotlinx.android.synthetic.main.activity_payments.*
import kotlinx.android.synthetic.main.content_payments.*

class PaymentsActivity : AbstractAsyncActivity(){

    private val requestCode : Int = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payments)

        setSupportActionBar(toolbar)

        serv_pay.setOnClickListener{startServicePayment()}
        state_pay.setOnClickListener{startTaxPayment()}
        ss_pay.setOnClickListener{startSSPayment()}
        qr_pay.setOnClickListener{startQRPayment()}
        scheduled_payments.setOnClickListener{startScheduled()}

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            val qr = data.getStringExtra("qrCode")
            handleQRCode(qr)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleQRCode(qr: String) {
        val validator = QRCodeValidator(qr)
        if (validator.validate()) {
            val intent =
                    if(validator.entity == "10095") Intent(this, TaxPaymentActivity::class.java)
                    else Intent(this, ServicePaymentActivity::class.java)
            intent.apply {
                putExtra("entity", validator.entity)
                putExtra("ref", validator.ref)
                putExtra("amount", validator.amount)
            }
            startActivity(intent)
        } else {
            Toast.makeText(this, getString(R.string.error_invalid_qr), Toast.LENGTH_LONG).show()
        }
    }

    private fun startServicePayment() = startActivity(Intent(this@PaymentsActivity, ServicePaymentActivity::class.java))

    private fun startTaxPayment() = startActivity(Intent(this@PaymentsActivity, TaxPaymentActivity::class.java))

    private fun startSSPayment() = startActivity(Intent(this@PaymentsActivity, SSPaymentActivity::class.java))

    private fun startQRPayment() = startActivityForResult(Intent(this@PaymentsActivity, QRPaymentActivity::class.java), requestCode)

    private fun startScheduled() = startActivity(Intent(this@PaymentsActivity, ScheduledPaymentsActivity::class.java))
}