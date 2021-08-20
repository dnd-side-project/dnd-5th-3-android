package how.about.it.view.feed.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import how.about.it.BR
import how.about.it.databinding.ItemFeedTopBinding
import how.about.it.util.TimeChangerUtil
import how.about.it.view.feed.Feed
import how.about.it.view.feed.FeedFragmentDirections

class FeedTopAdapter(feedDiffUtil: DiffUtil.ItemCallback<Feed>) :
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
}