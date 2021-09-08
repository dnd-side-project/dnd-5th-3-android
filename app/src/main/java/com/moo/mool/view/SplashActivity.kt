package com.moo.mool.view

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.*
import com.moo.mool.util.LoadingDialogUtil
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private val loginViewModel by viewModels<LoginViewModel>()
    private lateinit var loadingDialog : AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingDialog = LoadingDialogUtil.showLoadingIcon(this)
        loginViewModel.autoLogin()
        setSplashAutoLoginCollect()
    }

    private fun setSplashAutoLoginCollect() {
        loginViewModel.responseLogin.observe(this, Observer {
            if(it==true){
                this.lifecycleScope.launch {
                    this@SplashActivity.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                        with(loginViewModel){
                            loginSuccess
                                .collect() { loginSuccess ->
                                if(loginSuccess == true){
                                    val intent = Intent(applicationContext, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(applicationContext, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                                finish()
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        LoadingDialogUtil.hideLoadingIcon(loadingDialog)
        super.onDestroy()
    }
}