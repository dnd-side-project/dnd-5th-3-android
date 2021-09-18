package com.moo.mool.view.comment.adapter

import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.moo.mool.BR
import com.moo.mool.R
import com.moo.mool.databinding.ItemReCommentBinding
import com.moo.mool.util.DeleteDialogUtil
import com.moo.mool.util.TimeChangerUtil
import com.moo.mool.util.getIsMineUtil
import com.moo.mool.util.navigateWithData
import com.moo.mool.view.comment.CommentFragmentDirections
import com.moo.mool.view.comment.model.Comment
import com.moo.mool.view.comment.viewmodel.CommentViewModel

class ReplyAdapter(private val commentViewModel: CommentViewModel) :
    ListAdapter<Comment, ReplyAdapter.ReCommentViewHolder>(replyDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ReCommentViewHolder(
        ItemReCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), parent, commentViewModel
    )

    override fun onBindViewHolder(
        holder: ReCommentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ReCommentViewHolder(
        private val binding: ItemReCommentBinding,
        private val parent: ViewGroup,
        private val commentViewModel: CommentViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reply: Comment) {
            with(binding) {
                setVariable(BR.comment, reply)
                setVariable(BR.isMine, getIsMineUtil(parent.context, reply.writerName))
                executePendingBindings()
            }
            setCreatedAtText(reply.createdDate)
            setBtnMoreClickListener(reply)
        }

        private fun setCreatedAtText(createdAt: String) {
            binding.tvReCommentTime.text = TimeChangerUtil.timeChange(parent.context, createdAt)
        }

        private fun setBtnMoreClickListener(comment: Comment) {
            with(binding.btnReCommentMore) {
                setOnClickListener {
                    PopupMenu(
                        ContextThemeWrapper(
                            this.context,
                            R.style.feed_toggle_popup_menu
                        ),
                        this
                    ).apply {
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.menu_comment_update -> {
                                    navigateWithData(
                                        CommentFragmentDirections.actionCommentFragmentToCommentUpdateFragment(
                                            comment.commentId, comment.content
                                        )
                                    )
                                    true
                                }
                                R.id.menu_comment_delete -> {
                                    DeleteDialogUtil.showDeleteDialog(context, true) {
                                        commentViewModel.requestDeleteComment(comment.commentId)
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
        private val replyDiffUtil = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
                oldItem.commentId == newItem.commentId

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
                oldItem == newItem
        }
    }
}