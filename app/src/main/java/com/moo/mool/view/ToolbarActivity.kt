package com.moo.mool.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.moo.mool.R
import com.moo.mool.databinding.ActivityToolbarBinding

open class ToolbarActivity : AppCompatActivity() {
    private lateinit var toolbarViewBinding: ActivityToolbarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarViewBinding = ActivityToolbarBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_toolbar)
    }

    override fun setContentView(layoutResID: Int) {
        val fullView = toolbarViewBinding.root
        val activityContainer = toolbarViewBinding.flActivityContent
        layoutInflater.inflate(layoutResID, activityContainer, true)
        super.setContentView(fullView)

        val toolbar = toolbarViewBinding.toolbarBoard // 툴바 사용여부 결정
        if (useToolbar()) {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.common_google_signin_btn_icon_dark)
        } else {
            toolbar.visibility = View.GONE
        }
    }
    protected fun useToolbar(): Boolean {
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}