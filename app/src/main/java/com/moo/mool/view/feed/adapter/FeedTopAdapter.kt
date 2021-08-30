package com.moo.mool.view.feed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moo.mool.BR
import com.moo.mool.databinding.ItemFeedTopBinding
import com.moo.mool.util.TimeChangerUtil
import com.moo.mool.view.feed.Feed
import com.moo.mool.view.feed.FeedFragmentDirections

class FeedTopAdapter :
    ListAdapter<Feed, FeedTopAdapter.FeedTopViewHolder>(FEED_DIFF_UTIL) {
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
            setProgressTouchListener()
            setImageClipToOutLine()
        }

        private fun setBindingSetVariable(feed: Feed) {
            with(binding) {
                setVariable(BR.feed, feed)
                setVariable(BR.remainTime, TimeChangerUtil.getRemainTime(feed.voteDeadline))
                setVariable(BR.position, adapterPosition)
            }
        }

        private fun setRootClickListener(feedId: Int) {
            binding.root.setOnClickListener { view ->
                Navigation.findNavController(view).navigate(
                    FeedFragmentDirections.actionFeedFragmentToVoteFragment(
                        feedId,
                    )
                )
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
        private val FEED_DIFF_UTIL = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem == newItem
        }
    }
}
