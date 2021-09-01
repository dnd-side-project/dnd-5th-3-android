package com.moo.mool.util

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.moo.mool.R


object LoadingDialogUtil {
    fun showLoadingIcon(context: Context) : AlertDialog {
        val linearLayout = LinearLayout(context)
        linearLayout.gravity = Gravity.CENTER
        val progressBar = ProgressBar(context)
        progressBar.indeterminateDrawable = ContextCompat.getDrawable(context, R.drawable.animation_loading)
        progressBar.setBackgroundColor(Color.TRANSPARENT)

        val progressParam = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        progressParam.setMargins(0, 40, 0, 40) // set margin to progressBar

        progressBar.layoutParams = progressParam
        linearLayout.addView(progressBar)

        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder
            .setView(linearLayout)
            .setCancelable(false)

        val dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        return dialog
    }
    fun hideLoadingIcon(dialog: AlertDialog) {
        dialog.dismiss()
    }
}