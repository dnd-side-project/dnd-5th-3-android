package com.moo.mool.util

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.moo.mool.databinding.DialogDefaultConfirmBinding

object DefaultDialogUtil {
    fun createDialog(context: Context,
                        dialogTitleResId: Int, dialogDescriptionResId: Int,
                        confirmButtonShow: Boolean, cancelButtonShow: Boolean,
                        confirmButtonTextResId: Int?, cancelButtonTextResId: Int?,
                        confirmButtonFunction: (() -> Unit)?, cancelButtonFunction: (() -> Unit)?) : AlertDialog {
        val binding = DialogDefaultConfirmBinding.inflate(LayoutInflater.from(context))
        val dialogBuilder = AlertDialog.Builder(context).setView(binding.root)
        val mAlertDialog = dialogBuilder.create()
        mAlertDialog.setCancelable(false)

        with(binding){
            tvMessageDialogTitle.setText(dialogTitleResId)
            tvMessageDialogDescription.setText(dialogDescriptionResId)

            if(confirmButtonShow){
                tvDialogConfirm.visibility = View.VISIBLE
                confirmButtonTextResId?.let { tvDialogConfirm.setText(it) }
            } else {
                tvDialogConfirm.visibility = View.GONE
            }
            if(cancelButtonShow){
                tvDialogCancel.visibility = View.VISIBLE
                cancelButtonTextResId?.let { tvDialogCancel.setText(it) }
            } else {
                tvDialogCancel.visibility = View.GONE
            }

            tvDialogConfirm.setOnClickListener {
                if (confirmButtonFunction != null) {
                    confirmButtonFunction()
                }
                mAlertDialog.dismiss()
            }
            tvDialogCancel.setOnClickListener {
                if(cancelButtonFunction != null) {
                    cancelButtonFunction()
                }
                mAlertDialog.dismiss()
            }
        }

        return mAlertDialog
    }
}