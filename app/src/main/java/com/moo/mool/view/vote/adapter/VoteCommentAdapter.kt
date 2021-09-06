package com.moo.mool.view.vote.adapter

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moo.mool.BR
import com.moo.mool.R
import com.moo.mool.databinding.ItemCommentBinding
import com.moo.mool.util.DeleteDialogUtil
import com.moo.mool.view.comment.model.Comment
import com.moo.mool.view.comment.model.Emoji
import com.moo.mool.view.vote.VoteFragmentDirections
import com.moo.mool.view.vote.viewmodel.VoteViewModel

class VoteCommentAdapter(private val voteViewModel: VoteViewModel) :
    ListAdapter<Comment, VoteCommentAdapter.VoteCommentViewHolder>(COMMENT_DIFF_UTIL) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = VoteCommentViewHolder(
        ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), voteViewModel
    )

    override fun onBindViewHolder(
        holder: VoteCommentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class VoteCommentViewHolder(
        private val binding: ItemCommentBinding,
        private val voteViewModel: VoteViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        private var emojiList: MutableList<Emoji> = mutableListOf()

        fun bind(comment: Comment) {
            binding.setVariable(BR.comment, comment)
            setRootClickListener(comment.commentId)
            setBtnReactionEmptyListener(comment.commentId)
            setBtnMoreClickListener(comment)
            initEmojiList(comment.emojiList)
            initTvEmoji(comment.emojiList)
        }

        private fun setRootClickListener(commentId: Int) {
            binding.root.setOnClickListener { view ->
                Navigation.findNavController(view).navigate(
                    VoteFragmentDirections.actionVoteFragmentToCommentFragment(
                        commentId,
                    )
                )
            }
        }

        private fun setBtnReactionEmptyListener(commentId: Int) {
            binding.btnReactionEmpty.setOnClickListener { view ->
                Navigation.findNavController(view).navigate(
                    VoteFragmentDirections.actionVoteFragmentToCommentFragment(
                        commentId,
                        1
                    )
                )
            }
        }

        private fun initEmojiList(responseEmojiList: List<Emoji>) {
            responseEmojiList.forEach { responseEmoji ->
                emojiList.add(responseEmoji)
            }
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
            binding.btnReactionEmpty.visibility = when (sum) {
                0 -> View.VISIBLE
                else -> View.GONE
            }
        }

        private fun getLayoutEmoji(emojiId: Int): TextView {
            with(binding) {
                return when (emojiId) {
                    1 -> tvCommentReactionBrown
                    2 -> tvCommentReactionBlue
                    3 -> tvCommentReactionGreen
                    4 -> tvCommentReactionRed
                    5 -> tvCommentReactionYellow
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
                true -> this.setBackgroundResource(R.drawable.background_small_emoji_voted_round_20)
                false -> this.setBackgroundResource(R.drawable.background_small_emoji_round_20)
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
                                    Navigation.findNavController(binding.btnCommentMore)
                                        .navigate(
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
    }

    companion object {
        private val COMMENT_DIFF_UTIL = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem == newItem
        }
    }
}
