package com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.account

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs.AccountsListDialog
import kotlinx.android.synthetic.main.component_account_view.*

class AccountView
    : Fragment(), AccountContract.View, AccountsListDialog.OnSetAccountListener {

    private var presenter: AccountContract.Presenter? = null
    internal var listener: OnAccountSetListener? = null

    interface OnAccountSetListener {
        fun onAccountUpdate()
    }

    override fun onResume() {
        super.onResume()
        presenter!!.updateView()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_account.setOnClickListener{
            val aDialog = AccountsListDialog()
            aDialog.setTargetFragment(this, 0)
            aDialog.show(fragmentManager, "PAYMENTS")
        }
        val sharedPreferences = context.getSharedPreferences("com.example.rodrigobrodrigues.accentureHomeBanking",
                Context.MODE_PRIVATE)
        presenter = AccountPresenter(this, sharedPreferences)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.component_account_view, container)
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as OnAccountSetListener
        }
    }

    override fun setAccountHint(s: String) {
        btn_account.hint = s
    }

    override fun updateBalance(s: String) {
        val balanceText: String = getString(R.string.balance) + s
        balance.text = balanceText
    }

    override fun onSetAccount(position: Int) {
        presenter!!.setAccount(position)
        listener!!.onAccountUpdate()
    }
}