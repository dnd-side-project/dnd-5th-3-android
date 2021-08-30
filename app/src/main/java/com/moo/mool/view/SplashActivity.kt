package com.moo.mool.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.moo.mool.repository.LoginRepository
import com.moo.mool.view.login.LoginActivity
import com.moo.mool.view.main.MainActivity
import com.moo.mool.viewmodel.LoginViewModel
import com.moo.mool.viewmodel.LoginViewModelFactory

class SplashActivity : AppCompatActivity() {
    private lateinit var loginViewModel : LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(LoginRepository(this))).get(LoginViewModel::class.java)
        loginViewModel.autoLogin()
        loginViewModel.loginSuccess.observe(this, Observer {
            if(it) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

        })
        loginViewModel.loginFailedMessage.observe(this, Observer {
            Log.e("Login Error Activity", it.toString())
        })
    }
}