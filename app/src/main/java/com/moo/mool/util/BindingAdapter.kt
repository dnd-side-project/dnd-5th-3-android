package com.moo.mool.util

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
import com.moo.mool.R
import com.moo.mool.view.feed.model.Feed
import com.moo.mool.view.vote.model.ResponseFeedDetail

object BindingAdapter {
    @BindingAdapter("feedGlide")
    @JvmStatic
    fun feedGlide(imageView: ImageView, imageUrl: String?) {
        imageUrl?.let {
            when (imageUrl) {
                "" -> imageView.setImageResource(R.drawable.ic_feed_image_empty)
                else -> {
                    Glide.with(imageView.context)
                        .load(imageUrl)
                        .into(imageView)
                }
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

    @BindingAdapter(
        "feedProgressPermitDrawable",
        "feedProgressRejectDrawable",
        "feedProgressRemainTime"
    )
    @JvmStatic
    fun feedProgressPermitDrawable(
        seekBar: SeekBar,
        permitCount: Int?,
        rejectCount: Int?,
        time: Long?
    ) {
        permitCount?.let {
            rejectCount?.let {
                time?.let {
                    val context = seekBar.context
                    seekBar.progressDrawable = when (time) {
                        (-1).toLong() -> {
                            if (permitCount >= rejectCount) {
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
                            if (permitCount == 0 && rejectCount == 0) {
                                AppCompatResources.getDrawable(
                                    context,
                                    R.drawable.background_progress_empty_round_10
                                )
                            } else {
                                AppCompatResources.getDrawable(
                                    context,
                                    R.drawable.background_progress_round_10
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @BindingAdapter("feedThumbPermit", "feedThumbReject")
    @JvmStatic
    fun feedThumbPermit(seekBar: SeekBar, permitCount: Int?, rejectCount: Int) {
        permitCount?.let {
            if (permitCount == 0 && rejectCount == 0) {
                seekBar.thumb = AppCompatResources.getDrawable(
                    seekBar.context,
                    R.drawable.background_progress_empty_thumb
                )
            } else {
                if (permitCount >= rejectCount) {
                    seekBar.thumb = AppCompatResources.getDrawable(
                        seekBar.context,
                        R.drawable.ic_feed_thumb_agree
                    )
                } else {
                    seekBar.thumb = AppCompatResources.getDrawable(
                        seekBar.context,
                        R.drawable.ic_feed_thumb_disagree
                    )
                }
            }
        }
    }

    @BindingAdapter("voteThumbPermit", "voteThumbReject")
    @JvmStatic
    fun voteThumbPermit(seekBar: SeekBar, permitRatio: Int?, rejectRatio: Int) {
        permitRatio?.let {
            if (permitRatio == 0 && rejectRatio == 0) {
                seekBar.thumb.mutate().alpha = 0
            } else {
                if (permitRatio >= rejectRatio) {
                    seekBar.thumb = AppCompatResources.getDrawable(
                        seekBar.context,
                        R.drawable.ic_feed_thumb_agree
                    )
                } else {
                    seekBar.thumb = AppCompatResources.getDrawable(
                        seekBar.context,
                        R.drawable.ic_feed_thumb_disagree
                    )
                }
            }
        }
    }

    @BindingAdapter("feedVoteWriterCreatedText")
    @JvmStatic
    fun feedVoteWriterCreatedText(textView: TextView, feed: ResponseFeedDetail?) {
        feed?.let {
            textView.text = String.format(
                textView.context.getString(R.string.feed_item_nickname_time),
                feed.name,
                TimeChangerUtil.timeChange(textView.context, feed.createdDate)
            )
        }
    }
}