package com.moo.mool.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.moo.mool.model.RequestLogin
import com.moo.mool.model.RequestMember
import com.moo.mool.repository.LoginRepository
import com.moo.mool.repository.SignupRepository

class SignupViewModel(application: Application) : AndroidViewModel(application) {
    private var email = ""
    private var password = ""
    private var nickname = ""

    val signupSuccess = MutableLiveData<Boolean>()
    val signupFailedMessage = MutableLiveData<String?>()

    val duplicateCheckEmailSuccess = MutableLiveData<Boolean>()
    val duplicateCheckEmailFailedMessage = MutableLiveData<String?>()
    val duplicateCheckNicknameSuccess = MutableLiveData<Boolean>()
    val duplicateCheckNicknameFailedMessage = MutableLiveData<String?>()

    val loginSuccess = MutableLiveData<Boolean>()
    val loginFailedMessage = MutableLiveData<String?>()

    private val signupRepository = SignupRepository()
    private val loginRepository = LoginRepository(getApplication<Application>().applicationContext)

    fun setEmail(email : String) {
        this.email = email
    }
    fun getEmail() = email

    fun setPassword(password : String) {
        this.password = password
    }
    fun getPassword() = password

    fun setNickname(nickname : String) {
        this.nickname = nickname
    }
    fun getNickname() = nickname

    fun duplicateCheckEmail(email : String) {
        signupRepository.duplicateCheckEmail(email, object : SignupRepository.SignupCallBack {
            override fun onSuccess() {
                duplicateCheckEmailSuccess.postValue(true)
            }
            override fun onError(message: String?) {
                duplicateCheckEmailSuccess.postValue(false)
                duplicateCheckEmailFailedMessage.postValue(message)
            }
        })
    }

    fun duplicateCheckNickname(nickname: String) {
        signupRepository.duplicateCheckNickname(nickname, object : SignupRepository.SignupCallBack {
            override fun onSuccess() {
                duplicateCheckNicknameSuccess.postValue(true)
            }
            override fun onError(message: String?) {
                duplicateCheckNicknameSuccess.postValue(false)
                duplicateCheckNicknameFailedMessage.postValue(message)
            }
        })
    }

    fun signup() {
        signupRepository.signupUser(RequestMember(getEmail(), getPassword(), getNickname()), object : SignupRepository.SignupCallBack {
            override fun onSuccess() {
                signupSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                signupSuccess.postValue(false)
                signupFailedMessage.postValue(message)
            }
        })
    }

    fun login() {
        loginRepository.loginUser(RequestLogin(getEmail(), getPassword()), object : LoginRepository.LoginCallBack {
            override fun onSuccess() {
                loginSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                loginSuccess.postValue(false)
                loginFailedMessage.postValue(message)
            }
        })
    }
}