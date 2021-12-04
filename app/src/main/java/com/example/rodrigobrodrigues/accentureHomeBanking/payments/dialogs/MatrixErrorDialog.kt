package com.example.rodrigobrodrigues.accentureHomeBanking.payments.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.support.v4.app.DialogFragment
import android.os.Bundle
import com.example.rodrigobrodrigues.accentureHomeBanking.R

/**
 * @author Rodrigo Rodrigues on 19/02/2018.
 */

class MatrixErrorDialog : DialogFragment() {

    private var listener: MatrixErrorDialogListener? = null

    interface MatrixErrorDialogListener {
        fun onCreateMatrixClick()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        val activity: Activity

        if (context is Activity) {
            activity = context
            listener = activity as MatrixErrorDialog.MatrixErrorDialogListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.invalid_operation)
                .setIcon(R.drawable.ic_error_24dp)
                .setMessage(R.string.matrix_required)
                .setPositiveButton(R.string.create_matrix) { _, _ ->
                    dismiss()
                    listener!!.onCreateMatrixClick()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> dismiss() }
                .create()
    }
}
