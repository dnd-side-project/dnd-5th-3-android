package com.moo.mool.view.login

import android.os.Bundle
import com.moo.mool.databinding.ActivityLoginBinding
import com.moo.mool.view.ToolbarActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ToolbarActivity() {
    private lateinit var loginViewBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginViewBinding.root)
    }
}