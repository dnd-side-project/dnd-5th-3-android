package how.about.it.view.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import how.about.it.BR
import how.about.it.databinding.ItemFeedBottomBinding
import how.about.it.util.TimeChangerUtil
import how.about.it.view.feed.Feed
import how.about.it.view.mypage.MyPageFragmentDirections

class MyPageFeedAdapter :
    ListAdapter<Feed, MyPageFeedAdapter.MyPageFeedViewHolder>(FEED_DIFF_UTIL) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = MyPageFeedViewHolder(
        ItemFeedBottomBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyPageFeedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class MyPageFeedViewHolder(
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
                setVariable(BR.position, adapterPosition)
            }
        }

        private fun setRootClickListener(feedId: Int) {
            binding.root.setOnClickListener { view ->
                Navigation.findNavController(view).navigate(
                    MyPageFragmentDirections.actionMyPageFragmentToVoteFragment(
                        feedId,
                    )
                )
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
        private val FEED_DIFF_UTIL = object : DiffUtil.ItemCallback<Feed>() {
            override fun areItemsTheSame(oldItem: Feed, newItem: Feed) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Feed, newItem: Feed): Boolean =
                oldItem == newItem
        }
    }
}