package com.example.rodrigobrodrigues.accentureHomeBanking.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * @author Rodrigo Rodrigues on 22/03/2018.
 */

class FailureDialog : DialogFragment(){


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setIcon(R.drawable.ic_error_24dp)
                .setTitle(arguments.getString("title"))
                .setMessage(arguments.getString("body"))
                .setPositiveButton(getString(R.string.ok)) { _, _ -> dismiss() }
                .create()
    }
    companion object {
        fun newInstance(title: String, body: String): FailureDialog {
            val args = Bundle()
            args.putString("title", title)
            args.putString("body", body)
            val fragment = FailureDialog()
            fragment.arguments = args
            return fragment
        }
    }

}
