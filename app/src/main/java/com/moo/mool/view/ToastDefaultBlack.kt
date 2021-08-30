package com.moo.mool.view

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.moo.mool.R
import com.moo.mool.databinding.ToastDefaultBlackBinding

object ToastDefaultBlack {

    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ToastDefaultBlackBinding =
            DataBindingUtil.inflate(inflater, R.layout.toast_default_black, null, false)

        binding.tvToastMessage.text = message

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.CENTER, 0, 36.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}