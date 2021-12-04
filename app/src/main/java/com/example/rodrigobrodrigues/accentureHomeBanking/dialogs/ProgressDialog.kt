package com.example.rodrigobrodrigues.accentureHomeBanking.dialogs

import android.app.DialogFragment
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rodrigobrodrigues.accentureHomeBanking.R

class ProgressDialog : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        dialog.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        return inflater!!.inflate(R.layout.progress_bar, container)
    }
}
