package com.example.rodrigobrodrigues.accentureHomeBanking.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * Created by rodrigo.b.rodrigues on 05/04/2018.
 */

class SuccessDialog
    : DialogFragment(), View.OnClickListener {

    override fun onClick(view: View) {
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(arguments.getString("title"))
                .setMessage(arguments.getString("body"))
                .setIcon(R.drawable.ic_check_circle_24dp)
                .setPositiveButton(R.string.ok) { _, _ -> dismiss() }
                .create()
    }

    companion object {

        fun newInstance(title: String, body: String): SuccessDialog {
            val args = Bundle()
            args.putString("title", title)
            args.putString("body", body)
            val fragment = SuccessDialog()
            fragment.arguments = args
            return fragment
        }
    }
}