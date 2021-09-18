package com.moo.mool.view.vote.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moo.mool.BR
import com.moo.mool.R
import com.moo.mool.databinding.ItemCommentBinding
import com.moo.mool.util.DeleteDialogUtil
import com.moo.mool.util.TimeChangerUtil
import com.moo.mool.util.getIsMineUtil
import com.moo.mool.util.navigateWithData
import com.moo.mool.view.comment.model.Comment
import com.moo.mool.view.comment.model.Emoji
import com.moo.mool.view.vote.VoteFragmentDirections
import com.moo.mool.view.vote.viewmodel.VoteViewModel

class VoteCommentAdapter(private val voteViewModel: VoteViewModel) :
    ListAdapter<Comment, VoteCommentAdapter.VoteCommentViewHolder>(commentDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = VoteCommentViewHolder(
        ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), parent, voteViewModel
    )

    override fun onBindViewHolder(
        holder: VoteCommentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class VoteCommentViewHolder(
        private val binding: ItemCommentBinding,
        private val parent: ViewGroup,
        private val voteViewModel: VoteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        private var emojiList: MutableList<Emoji> = mutableListOf()

        fun bind(comment: Comment) {
            with(binding) {
                setVariable(BR.comment, comment)
                setVariable(BR.isMine, getIsMineUtil(parent.context, comment.writerName))
                executePendingBindings()
            }
            setCreatedAtText(comment.createdDate)
            setRootClickListener(comment.commentId)
            setBtnReactionEmptyListener(comment.commentId)
            setBtnMoreClickListener(comment)
            initEmojiList(comment.emojiList)
            initTvEmoji(comment.emojiList)
        }

        private fun setCreatedAtText(createdAt: String) {
            binding.tvCommentTime.text = TimeChangerUtil.timeChange(parent.context, createdAt)
        }

        private fun setRootClickListener(commentId: Int) {
            binding.root.setOnClickListener { view ->
                view.navigateWithData(
                    VoteFragmentDirections.actionVoteFragmentToCommentFragment(
                        commentId,
                    )
                )
            }
        }

        private fun setBtnReactionEmptyListener(commentId: Int) {
            binding.btnEmojiEmpty.setOnClickListener { view ->
                view.navigateWithData(
                    VoteFragmentDirections.actionVoteFragmentToCommentFragment(
                        commentId,
                        "OPEN"
                    )
                )
            }
        }

        private fun setBtnMoreClickListener(comment: Comment) {
            with(binding.btnCommentMore) {
                setOnClickListener {
                    PopupMenu(
                        ContextThemeWrapper(
                            context,
                            R.style.feed_toggle_popup_menu
                        ),
                        this
                    ).apply {
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.menu_comment_update -> {
                                    navigateWithData(
                                        VoteFragmentDirections.actionVoteFragmentToCommentUpdateFragment(
                                            comment.commentId, comment.content
                                        )
                                    )
                                    true
                                }
                                R.id.menu_comment_delete -> {
                                    DeleteDialogUtil.showDeleteDialog(context, true) {
                                        voteViewModel.requestDeleteComment(comment.commentId)
                                    }
                                    true
                                }
                                else -> false
                            }
                        }
                        inflate(R.menu.menu_comment)
                        show()
                    }
                }
            }
        }

        private fun initEmojiList(responseEmojiList: List<Emoji>) {
            emojiList.addAll(responseEmojiList)
        }

        private fun initTvEmoji(emojiList: List<Emoji>) {
            var sum = 0
            emojiList.indices.forEach { index ->
                with(emojiList[index]) {
                    sum += emojiCount
                    getLayoutEmoji(emojiId).apply {
                        setTvCommentReactVisibility(emojiCount)
                        setTvCommentReactCount(emojiCount)
                        setTvCommentReactClickListener(emojiList[index], index)
                        setTvCommentReactionBackground(checked)
                    }
                }
            }
            setEmptyCommentReactVisibility(sum)
        }

        private fun setEmptyCommentReactVisibility(sum: Int) {
            binding.btnEmojiEmpty.visibility = when (sum) {
                0 -> View.VISIBLE
                else -> View.GONE
            }
        }

        private fun getLayoutEmoji(emojiId: Int): TextView {
            with(binding) {
                return when (emojiId) {
                    1 -> tvCommentEmojiBrown
                    2 -> tvCommentEmojiBlue
                    3 -> tvCommentEmojiGreen
                    4 -> tvCommentEmojiRed
                    5 -> tvCommentEmojiYellow
                    else -> throw IndexOutOfBoundsException()
                }
            }
        }

        private fun TextView.setTvCommentReactVisibility(emojiCount: Int) {
            this.visibility = when (emojiCount) {
                0 -> View.GONE
                else -> View.VISIBLE
            }
        }

        private fun TextView.setTvCommentReactCount(emojiCount: Int) {
            this.text = emojiCount.toString()
        }

        private fun TextView.setTvCommentReactClickListener(emoji: Emoji, selected: Int) {
            this.setOnClickListener {
                voteViewModel.requestPutEmoji(emoji.commentEmojiId, !emoji.checked)
                applyPutEmoji(selected)
                initTvEmoji(emojiList)
            }
        }

        private fun applyPutEmoji(selected: Int) {
            emojiList = emojiList.mapIndexed { index, emoji ->
                when (index) {
                    selected -> emoji.copy(
                        emojiCount = setEmojiCount(emoji),
                        checked = !emoji.checked
                    )
                    else -> emoji
                }
            }.toMutableList()
        }

        private fun setEmojiCount(emoji: Emoji) = when (emoji.checked) {
            true -> emoji.emojiCount - 1
            false -> emoji.emojiCount + 1
        }

        private fun TextView.setTvCommentReactionBackground(checked: Boolean) {
            when (checked) {
                true -> this.setBackgroundResource(R.drawable.background_small_emoji_checked_round_20)
                false -> this.setBackgroundResource(R.drawable.background_small_emoji_round_20)
            }
        }
    }

    companion object {
        private val commentDiffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem == newItem
        }
    }
}
