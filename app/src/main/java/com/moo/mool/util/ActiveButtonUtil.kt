package com.moo.mool.util

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat

object ActiveButtonUtil {
    fun setButtonState(context : Context, baseButton : AppCompatButton, buttonActivation: Boolean, backgroundColorResId: Int?, textColorResId: Int?) {
        baseButton.isEnabled = buttonActivation
        baseButton.background = backgroundColorResId?.let { ContextCompat.getDrawable(context, it) }
        baseButton.setTextColor(textColorResId?.let { context.resources.getColorStateList(it, context.theme) })
    }
}