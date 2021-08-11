package how.about.it.util

import android.animation.ObjectAnimator
import androidx.constraintlayout.widget.ConstraintLayout

object FloatingAnimationUtil {
    fun setAnimation(layout: ConstraintLayout, value: Float) {
        ObjectAnimator.ofFloat(layout, "translationY", value).start()
    }
}