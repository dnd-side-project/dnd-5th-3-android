package com.moo.mool.view.feed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moo.mool.BR
import com.moo.mool.databinding.ItemFeedBottomBinding
import com.moo.mool.util.TimeChangerUtil
import com.moo.mool.util.navigateWithData
import com.moo.mool.view.feed.FeedFragmentDirections
import com.moo.mool.view.feed.model.Feed

class FeedBottomAdapter :
    ListAdapter<Feed, FeedBottomAdapter.FeedBottomViewHolder>(feedDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FeedBottomViewHolder(
        ItemFeedBottomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FeedBottomViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedBottomViewHolder(
        private val binding: ItemFeedBottomBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(feed: Feed) {
            setBindingSetVariable(feed)
            setRootClickListener(feed.id)
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

        @SuppressLint("ClickableViewAccessibility")
        private fun setProgressTouchListener() {
            binding.progressFeedBottom.setOnTouchListener { _, _ -> true }
        }

        private fun setImageClipToOutLine() {
            binding.imgFeedBottom.clipToOutline = true
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
