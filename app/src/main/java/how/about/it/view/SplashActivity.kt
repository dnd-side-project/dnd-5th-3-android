package how.about.it.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import how.about.it.repository.LoginRepository
import how.about.it.view.login.LoginActivity
import how.about.it.view.main.MainActivity
import how.about.it.viewmodel.LoginViewModel
import how.about.it.viewmodel.LoginViewModelFactory

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