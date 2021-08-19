package how.about.it.view.vote.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.view.ContextThemeWrapper
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import how.about.it.BR
import how.about.it.R
import how.about.it.databinding.ItemCommentBinding
import how.about.it.view.comment.Comment
import how.about.it.view.vote.VoteFragmentDirections
import how.about.it.view.vote.viewmodel.VoteViewModel

class VoteCommentAdapter(private val voteViewModel: VoteViewModel) :
    ListAdapter<Comment, VoteCommentAdapter.VoteCommentViewHolder>(VoteCommentDiffUtil()) {
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
        fun bind(comment: Comment) {
            binding.setVariable(BR.comment, comment)
            setRootClickListener(comment.commentId)
            setBtnMoreClickListener(comment)
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

        private fun setBtnMoreClickListener(comment: Comment) {
            with(binding.btnCommentMore) {
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
                                    Navigation.findNavController(binding.btnCommentMore).navigate(
                                        VoteFragmentDirections.actionVoteFragmentToCommentUpdateFragment(
                                            comment.commentId, comment.content
                                        )
                                    )
                                    true
                                }
                                R.id.menu_comment_delete -> {
                                    requestCommentDeleteDialog(
                                        context,
                                        comment.commentId
                                    )
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

        private fun requestCommentDeleteDialog(context: Context, id: Int) {
            val mDialogView =
                LayoutInflater.from(context).inflate(R.layout.dialog_default_confirm, null)
            val mBuilder = AlertDialog.Builder(context)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_title)
                .setText(R.string.delete)
            mDialogView.findViewById<TextView>(R.id.tv_message_dialog_description)
                .setText(R.string.comment_delete_dialog)

            mDialogView.findViewById<Button>(R.id.btn_dialog_confirm).setOnClickListener {
                voteViewModel.requestDeleteComment(id)
                mAlertDialog.dismiss()
            }
            mDialogView.findViewById<Button>(R.id.btn_dialog_cancel).setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private class VoteCommentDiffUtil : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
            oldItem.commentId == newItem.commentId

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
            oldItem == newItem
    }
}
