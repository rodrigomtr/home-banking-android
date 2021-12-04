package com.example.rodrigobrodrigues.accentureHomeBanking.payments.scheduled

import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.ScheduledPayment
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.BasePaymentView
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.ScheduledInfoDialog
import kotlinx.android.synthetic.main.activity_scheduled_payments.*
import kotlinx.android.synthetic.main.content_scheduled_payments.*

/**
 * @author Rodrigo Rodrigues on 15/03/2018.
 */
class ScheduledPaymentsActivity : BasePaymentView(), ScheduledPaymentContract.View, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    private var presenter: ScheduledPaymentContract.Presenter? = null

    private var listAdapter: ScheduledPaymentAdapter? = null

    private var deleteItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduled_payments)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.presenter = ScheduledPaymentPresenter(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.tools, menu)
        deleteItem = menu.getItem(0)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                presenter!!.delete()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun showError() {
        tv_error.visibility = View.VISIBLE
    }

    override fun setListView(catalog: List<ScheduledPayment>) {
        listAdapter = ScheduledPaymentAdapter(this, catalog)
        list_view.adapter = listAdapter
        list_view.onItemLongClickListener = this
        list_view.onItemClickListener = this
    }

    override fun selectFirstItem(view: View) {
        listAdapter!!.setSelected(true)
        listAdapter!!.notifyDataSetChanged()
        list_view.requestFocus()
        val cb = view.findViewById<CheckBox>(R.id.checkBox)
        cb.isChecked = true
        view.setBackgroundColor(ContextCompat.getColor(this, R.color.selected))
        deleteItem!!.isVisible = true

    }

    override fun deselectItem(view: View) {
        val cb = view.findViewById<CheckBox>(R.id.checkBox)
        cb.isChecked = false
        view.setBackgroundColor(Color.TRANSPARENT)
    }

    override fun cleanSelectingView() {
        listAdapter!!.setSelected(false)
        listAdapter!!.notifyDataSetChanged()
        deleteItem!!.isVisible = false
    }

    override fun selectItem(view: View) {
        val cb = view.findViewById<CheckBox>(R.id.checkBox)
        cb.isChecked = true
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.selected))
    }

    override fun updateList(scheduledPayment: ScheduledPayment) {
        listAdapter!!.remove(scheduledPayment)
        listAdapter!!.notifyDataSetChanged()
    }

    override fun showDeleteFailureDialog() {
        super.showFailureDialog(getString(R.string.error_title),
                getString(R.string.error_delete_scheduled_subtitle) + "\n" +
                        getString(R.string.error_try_again))
    }

    override fun showInfoDialog(scheduledPayment: ScheduledPayment) {
        val dialog = ScheduledInfoDialog.newInstance(
                scheduledPayment.account,
                scheduledPayment.entity,
                scheduledPayment.ref,
                scheduledPayment.amount,
                scheduledPayment.description)
        dialog.show(supportFragmentManager, "SCHEDULED_INFO")
    }

    override fun onItemLongClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long): Boolean {
        presenter!!.listLongClick(i, view)
        return true
    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        presenter!!.listClick(i, view)
    }
}
