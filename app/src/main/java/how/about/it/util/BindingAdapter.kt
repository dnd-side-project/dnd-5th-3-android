package how.about.it.util

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import how.about.it.R
import how.about.it.view.feed.Feed

object BindingAdapter {
    @BindingAdapter("feedTopTitleText")
    @JvmStatic
    fun feedTopTitleText(textView: TextView, position: Int) {
        textView.text = when (position) {
            0 -> textView.context.getString(R.string.feed_item_title1)
            1 -> textView.context.getString(R.string.feed_item_title2)
            2 -> textView.context.getString(R.string.feed_item_title3)
            3 -> textView.context.getString(R.string.feed_item_title4)
            4 -> textView.context.getString(R.string.feed_item_title5)
            else -> throw IndexOutOfBoundsException()
        }
    }

    @BindingAdapter("feedGlide")
    @JvmStatic
    fun feedGlide(imageView: ImageView, imageUrl: String) {
        when (imageUrl) {
            "" -> imageView.setImageResource(R.drawable.ic_feed_image_empty)
            else -> {
                Glide.with(imageView.context)
                    .load(imageUrl)
                    .into(imageView)
            }
        }
    }

    @BindingAdapter("feedWriterText", "feedTimeText")
    @JvmStatic
    fun feedWriterTimeText(textView: TextView, feed: Feed, time: Long) {
        val context = textView.context
        textView.text = when (time) {
            (-1).toLong() -> {
                if (feed.permitRatio >= feed.rejectRatio) {
                    SpannableStringBuilder(
                        String.format(
                            context.getString(R.string.feed_item_nickname_time),
                            feed.name,
                            context.getString(R.string.feed_item_agree)
                        )
                    ).apply {
                        setSpan(
                            ForegroundColorSpan(
                                context.getColor(R.color.moomool_blue_0098ff)
                            ),
                            this.length - 2,
                            this.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            this.length - 2,
                            this.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                } else {
                    SpannableStringBuilder(
                        String.format(
                            context.getString(R.string.feed_item_nickname_time),
                            feed.name,
                            context.getString(R.string.feed_item_disagree)
                        )
                    ).apply {
                        setSpan(
                            ForegroundColorSpan(
                                context.getColor(R.color.moomool_pink_ff227c)
                            ),
                            this.length - 2,
                            this.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        setSpan(
                            StyleSpan(Typeface.BOLD),
                            this.length - 2,
                            this.length,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
            else ->
                String.format(
                    textView.context.getString(R.string.feed_item_nickname_time),
                    feed.name,
                    String.format(textView.context.getString(R.string.feed_item_time), time)
                )
        }
    }

    @BindingAdapter("feedProgressDrawable", "feedProgressRemainTime")
    @JvmStatic
    fun feedProgressDrawable(seekBar: SeekBar, feed: Feed, time: Long) {
        val context = seekBar.context
        seekBar.progressDrawable = when (time) {
            (-1).toLong() -> {
                if (feed.permitRatio >= feed.rejectRatio) {
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.background_progress_permit_round_10
                    )
                } else {
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.background_progress_reject_round_10
                    )
                }
            }
            else -> {
                AppCompatResources.getDrawable(context, R.drawable.background_progress_round_10)
            }
        }
    }
}