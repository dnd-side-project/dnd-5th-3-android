package com.moo.mool.view.feed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moo.mool.BR
import com.moo.mool.R
import com.moo.mool.databinding.ItemFeedTopBinding
import com.moo.mool.util.TimeChangerUtil
import com.moo.mool.util.navigateWithData
import com.moo.mool.view.feed.FeedFragmentDirections
import com.moo.mool.view.feed.model.Feed

class FeedTopAdapter :
    ListAdapter<Feed, FeedTopAdapter.FeedTopViewHolder>(feedDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FeedTopViewHolder(
        ItemFeedTopBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FeedTopViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedTopViewHolder(
        private val binding: ItemFeedTopBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(feed: Feed) {
            setBindingSetVariable(feed)
            setRootClickListener(feed.id)
            setTopTitleText()
            setProgressTouchListener()
            setImageClipToOutLine()
        }

        private fun setBindingSetVariable(feed: Feed) {
            with(binding) {
                setVariable(BR.feed, feed)
                setVariable(BR.remainTime, TimeChangerUtil.getRemainTime(feed.voteDeadline))
                executePendingBindings()
            }
        }

        private fun setRootClickListener(feedId: Int) {
            binding.root.setOnClickListener {
                it.navigateWithData(FeedFragmentDirections.actionFeedFragmentToVoteFragment(feedId))
            }
        }

        private fun setTopTitleText() {
            with(binding.tvFeedTopCategory) {
                text = when (adapterPosition) {
                    0 -> context.getString(R.string.feed_item_title1)
                    1 -> context.getString(R.string.feed_item_title2)
                    2 -> context.getString(R.string.feed_item_title3)
                    3 -> context.getString(R.string.feed_item_title4)
                    4 -> context.getString(R.string.feed_item_title5)
                    else -> throw IndexOutOfBoundsException()
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setProgressTouchListener() {
            binding.progressFeedTop.setOnTouchListener { _, _ -> true }
        }

        private fun setImageClipToOutLine() {
            binding.imgFeedTop.clipToOutline = true
        }
    }

    companion object {
        private val feedDiffUtil = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem == newItem
        }
    }
}
