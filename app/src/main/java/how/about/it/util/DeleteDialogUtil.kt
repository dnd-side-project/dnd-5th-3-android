package how.about.it.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import how.about.it.R
import how.about.it.databinding.DialogDefaultConfirmBinding

object DeleteDialogUtil {
    fun showDeleteDialog(context: Context, isComment: Boolean, onClick: () -> Unit) {
        val binding = DialogDefaultConfirmBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context).setView(binding.root).show()

        with(binding) {
            tvMessageDialogTitle.text = context.getString(R.string.delete)
            tvMessageDialogDescription.text = when (isComment) {
                true -> context.getString(R.string.comment_delete_dialog)
                false -> context.getString(R.string.vote_delete_dialog)
            }
            btnDialogConfirm.setOnClickListener {
                onClick()
                dialogBuilder.dismiss()
            }
            btnDialogCancel.setOnClickListener {
                dialogBuilder.dismiss()
            }
        }
    }
}