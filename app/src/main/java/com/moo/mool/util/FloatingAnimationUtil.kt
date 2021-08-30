package com.moo.mool.util

import android.animation.ObjectAnimator
import android.content.Context
import android.util.TypedValue
import androidx.constraintlayout.widget.ConstraintLayout

object FloatingAnimationUtil {
    fun setAnimation(layout: ConstraintLayout, value: Float) {
        ObjectAnimator.ofFloat(layout, "translationY", dpToPx(layout.context, value)).start()
    }

    private fun dpToPx(context: Context, dp: Float) =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
}