package how.about.it.view.comment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.view.ContextThemeWrapper
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import how.about.it.BR
import how.about.it.R
import how.about.it.databinding.ItemReCommentBinding
import how.about.it.util.DeleteDialogUtil
import how.about.it.view.comment.Comment
import how.about.it.view.comment.CommentFragmentDirections
import how.about.it.view.comment.viewmodel.CommentViewModel

class ReCommentAdapter(private val commentViewModel: CommentViewModel) :
    ListAdapter<Comment, ReCommentAdapter.ReCommentViewHolder>(ReCommentDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ReCommentViewHolder(
        ItemReCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ), commentViewModel
    )

    override fun onBindViewHolder(
        holder: ReCommentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ReCommentViewHolder(
        private val binding: ItemReCommentBinding,
        private val commentViewModel: CommentViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reComment: Comment) {
            binding.setVariable(BR.comment, reComment)
            setBtnMoreClickListener(reComment)
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
                                    Navigation.findNavController(binding.btnReCommentMore)
                                        .navigate(
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

    private class ReCommentDiffUtil : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
            oldItem.commentId == newItem.commentId

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
            oldItem == newItem
    }
}