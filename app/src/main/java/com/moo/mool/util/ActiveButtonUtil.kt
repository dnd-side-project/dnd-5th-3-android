package com.moo.mool.util

import android.content.Context
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.ContextCompat
import com.moo.mool.R

object ActiveButtonUtil {
    fun setButtonState(context : Context, baseButton : AppCompatButton, buttonActivation: Boolean, backgroundColorResId: Int?, textColorResId: Int?) {
        baseButton.isEnabled = buttonActivation
        baseButton.background = backgroundColorResId?.let { ContextCompat.getDrawable(context, it) }
        baseButton.setTextColor(textColorResId?.let { context.resources.getColorStateList(it, context.theme) })
    }
    fun setButtonActive(context: Context, baseButton: AppCompatButton) {
        baseButton.isEnabled = true
        baseButton.background = ContextCompat.getDrawable(context, R.drawable.button_default_enable)
        baseButton.setTextColor(context.resources.getColorStateList(R.color.bluegray50_F9FAFC, context?.theme))
    }
    fun setButtonDeactivate(context: Context, baseButton: AppCompatButton) {
        baseButton.isEnabled = true
        baseButton.background = ContextCompat.getDrawable(context, R.drawable.button_default_disable)
        baseButton.setTextColor(context.resources.getColorStateList(R.color.bluegray600_626670, context?.theme))
    }
    fun setClearButton(clearButton: AppCompatImageButton, clearEditText : EditText) {
        clearButton.setOnClickListener {
            clearEditText.setText("")
        }
    }
}