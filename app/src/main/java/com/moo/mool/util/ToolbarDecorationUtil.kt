package com.moo.mool.util

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.moo.mool.R
import com.moo.mool.databinding.ActivityToolbarBinding

object ToolbarDecorationUtil {
    fun setToolbarDetail(toolbarBoard : ActivityToolbarBinding, titleStringResId: Int, baseActivity : Activity, baseFragment: Fragment) {
        // setTitle
        toolbarBoard.tvToolbarTitle.setText(titleStringResId)
        (baseActivity as AppCompatActivity).setSupportActionBar(toolbarBoard.toolbarBoard)
        baseActivity.supportActionBar?.setDisplayShowTitleEnabled(false)

        // showBackButton
        baseActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        baseActivity.supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
        baseFragment.setHasOptionsMenu(true)
    }
}