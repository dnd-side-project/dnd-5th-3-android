package com.moo.mool.util

import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.moo.mool.databinding.DialogDefaultConfirmBinding

object DefaultDialogUtil {
    lateinit var mAlertDialog : AlertDialog

    fun createDialog (context: Context, binding: DialogDefaultConfirmBinding) : AlertDialog{
        val dialogBuilder = AlertDialog.Builder(context).setView(binding.root)
        mAlertDialog = dialogBuilder.create()
        mAlertDialog.setCancelable(false)

        return mAlertDialog
    }

    fun setDialogDetail(binding: DialogDefaultConfirmBinding,
                        dialogTitleResId: Int, dialogDescriptionResId: Int,
                        confirmButtonShow: Boolean, cancelButtonShow: Boolean,
                        confirmButtonTextResId: Int?, cancelButtonTextResId: Int?) {
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
        }
    }
}