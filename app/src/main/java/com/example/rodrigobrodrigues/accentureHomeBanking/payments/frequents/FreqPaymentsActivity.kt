package com.example.rodrigobrodrigues.accentureHomeBanking.payments.frequents

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView

import com.example.rodrigobrodrigues.accentureHomeBanking.AbstractAsyncActivity
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.PaymentSender
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.account.AccountView
import kotlinx.android.synthetic.main.content_freq_payments.*
import kotlinx.android.synthetic.main.activity_freq_payments.*

class FreqPaymentsActivity
    : AbstractAsyncActivity(),
        FreqPaymentsContract.View,
        AdapterView.OnItemClickListener,
        AccountView.OnAccountSetListener {

    private var presenter: FreqPaymentsContract.Presenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_freq_payments)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences("com.example.rodrigobrodrigues.accentureHomeBanking",
                Context.MODE_PRIVATE)
        val parent = intent.getStringExtra("PARENT")
        this.presenter = FreqPaymentsPresenter(this, parent, sharedPreferences)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showError() {
        tv_error.visibility = View.VISIBLE
        tv_error.setText(R.string.error_no_freq_payments)
        tv_entity.visibility = View.GONE
        tv_ref.visibility = View.GONE
        tv_amount.visibility = View.GONE
        freq_payments.visibility = View.GONE
    }

    override fun setListView(payments: List<PaymentSender>) {
        tv_error.visibility = View.GONE
        freq_payments.visibility = View.VISIBLE
        val listAdapter = FreqPayListAdapter(this, payments)
        freq_payments.adapter = listAdapter
        freq_payments.onItemClickListener = this
        tv_entity.visibility = View.VISIBLE
        tv_ref.visibility = View.VISIBLE
        tv_amount.visibility = View.VISIBLE
    }

    override fun setResult(entity: String, ref: String, amount: String) {
        val intent = Intent()
        intent.putExtra("entity", entity)
        intent.putExtra("ref", ref)
        intent.putExtra("amount", amount)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        presenter!!.itemClicked(adapterView.getItemAtPosition(i) as PaymentSender)
    }

    override fun onAccountUpdate() {
        presenter!!.handleFrequents()
    }
}
