package com.example.rodrigobrodrigues.accentureHomeBanking.payments.components.account

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

import com.example.rodrigobrodrigues.accentureHomeBanking.R
import com.example.rodrigobrodrigues.accentureHomeBanking.models.Account
import com.example.rodrigobrodrigues.accentureHomeBanking.util.AccountHandler

class AccountsAdapter(private val accountList: List<Account>, private val context: Context)
    : BaseAdapter() {

    override fun getCount(): Int {
        return accountList.size
    }

    override fun getItem(i: Int): Account {
        return accountList[i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("InflateParams")
    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View {
        var viewResult = view
        if (viewResult == null) {
            viewResult = LayoutInflater.from(context).inflate(R.layout.accounts_card_item, null)

            val card = viewResult!!.findViewById<ImageView>(R.id.iv_card)
            val accountId = viewResult.findViewById<TextView>(R.id.account_id)

            accountId.text = AccountHandler.displayAccountId(accountList[i].id)

            val accountType = accountList[i].type
            var imageSrc = R.drawable.blue_background
            when (accountType) {
                "DEBIT" -> imageSrc = R.drawable.blue_background
                "CREDIT" -> imageSrc = R.drawable.red_background
                "GOLD" -> imageSrc = R.drawable.gold_background
            }

            //Round Card corners
            val mBitmap = (ContextCompat.getDrawable(viewResult.context, imageSrc) as BitmapDrawable).bitmap
            val imageRounded = Bitmap.createBitmap(1200, 750, mBitmap.config)
            val canvas = Canvas(imageRounded)
            val mPaint = Paint()
            mPaint.isAntiAlias = true
            mPaint.shader = BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            canvas.drawRoundRect(RectF(0f, 0f, 1200f, 750f), 100f, 100f, mPaint)// Round Image Corner 100 100 100 100
            card.setImageBitmap(imageRounded)
        }
        return viewResult
    }
}
