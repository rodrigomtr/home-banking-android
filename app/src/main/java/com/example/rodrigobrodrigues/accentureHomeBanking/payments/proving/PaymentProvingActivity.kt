package com.example.rodrigobrodrigues.accentureHomeBanking.payments.proving

import android.app.AlertDialog
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.home.MainActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentView
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.EmailProvingDialog
import com.example.rodrigobrodrigues.accentureHomeBanking.util.AccountHandler
import com.example.rodrigobrodrigues.accentureHomeBanking.util.CurrencyHandler
import kotlinx.android.synthetic.main.activity_payment_proving.*
import kotlinx.android.synthetic.main.content_payment_proving.*

class PaymentProvingActivity
    : BasePaymentView(), PaymentProvingContract.View, EmailProvingDialog.OnPositiveListener {
    override val downloadManager: DownloadManager
        get() = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

    private var presenter: PaymentProvingContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_proving)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val paymentId = intent.getIntExtra("paymentId", 0)

        this.presenter = PaymentProvingPresenter(this, paymentId)

        loadViews()
    }

    private fun loadViews() {

        val account = intent.getLongExtra("account", 0)
        val entity = intent.getStringExtra("entity")
        val ref = intent.getStringExtra("ref")
        val amount = intent.getDoubleExtra("amount", 0.0)
        val date = try {
            intent.getStringExtra("date").substring(0, 10)
        } catch (e: StringIndexOutOfBoundsException) {
            intent.getStringExtra("date")
        }

        tv_account.text = AccountHandler.displayAccountId(account)
        tv_entity.text = entity
        if (ref != null) {
            tv_ref.text = ref.replace("[\\w]{3}".toRegex(), "$0 ")
        } else {
            tv_ref.visibility = View.GONE
            textView8.visibility = View.GONE
        }
        tv_amount.text = CurrencyHandler.displayCurrency(amount)
        tv_date.text = date

        btn_download.setOnClickListener{presenter!!.downloadPdf()}
        btn_send_email.setOnClickListener{
            val emailProvingDialog = EmailProvingDialog()
            emailProvingDialog.show(supportFragmentManager, "PROVING")
        }
    }

    override fun onSendProving(email: String, name: String, description: String) {
        presenter!!.sendEmail(email, name, description)
    }

    override fun onBackPressed() {
        showAlertDialog()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> showAlertDialog()
        }
        return true
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this)
                .setMessage(R.string.back_to_home_message)
                .setTitle(R.string.back_to_home_title)
                .setPositiveButton(R.string.ok) { _, _ -> startActivity(Intent(this@PaymentProvingActivity, MainActivity::class.java)) }
                .setNegativeButton(R.string.cancel) { _, _ -> }
                .create()
                .show()
    }

    override fun showEmailSuccess() {
        super.showSuccessDialog(getString(R.string.success_email_sent_title),
                getString(R.string.success_email_sent_body))
    }

    override fun showEmailError() {
        super.showFailureDialog(getString(R.string.error_title),
                getString(R.string.failure_email_sent_body))
    }
}
