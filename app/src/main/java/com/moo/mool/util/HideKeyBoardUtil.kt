package com.moo.mool.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object HideKeyBoardUtil {
    fun hide(context: Context, editText: EditText) {
        editText.clearFocus()
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}