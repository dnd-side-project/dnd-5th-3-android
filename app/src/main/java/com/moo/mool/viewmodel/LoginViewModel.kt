package com.moo.mool.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moo.mool.model.RequestLogin
import com.moo.mool.repository.LoginRepository
class LoginViewModelFactory(val loginRepository: LoginRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(loginRepository::class.java)
            .newInstance(loginRepository)
    }
}

class LoginViewModel(private val loginRepository: LoginRepository): ViewModel() {
    val loginSuccess = MutableLiveData<Boolean>()
    val loginFailedMessage = MutableLiveData<String?>()

    fun login(requestLogin: RequestLogin) {
        loginRepository.loginUser(requestLogin, object : LoginRepository.LoginCallBack {
            override fun onSuccess() {
                loginSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                loginSuccess.postValue(false)
                loginFailedMessage.postValue(message)
            }
        })
    }

    fun autoLogin() {
        loginRepository.autoLogin(object : LoginRepository.LoginCallBack {
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