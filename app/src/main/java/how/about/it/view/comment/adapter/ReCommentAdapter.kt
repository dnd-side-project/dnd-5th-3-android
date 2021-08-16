package how.about.it.view.comment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import how.about.it.BR
import how.about.it.databinding.ItemReCommentBinding
import how.about.it.view.comment.Comment

class ReCommentAdapter :
    ListAdapter<Comment, ReCommentAdapter.ReCommentViewHolder>(ReCommentDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ReCommentViewHolder(
        ItemReCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ReCommentViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class ReCommentViewHolder(
        private val binding: ItemReCommentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reComment: Comment) {
            binding.setVariable(BR.comment, reComment)
        }
    }

    private class ReCommentDiffUtil : DiffUtil.ItemCallback<Comment>() {
        override fun areItemsTheSame(oldItem: Comment, newItem: Comment) =
            oldItem.commentId == newItem.commentId

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
            oldItem == newItem
    }
}