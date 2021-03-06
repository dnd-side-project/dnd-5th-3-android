package com.moo.mool.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.model.RequestLogin
import com.moo.mool.model.ResponseLogin
import com.moo.mool.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
    ): ViewModel() {

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()
    val responseLogin = MutableLiveData<Boolean?>()
    val loginFailedMessage = MutableLiveData<String?>()

    private val _resetPasswordSuccess = MutableStateFlow(false)
    val resetPasswordSuccess = _resetPasswordSuccess.asStateFlow()

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError.asStateFlow()

    fun login(requestLogin: RequestLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                loginRepository.loginUser(requestLogin)
            }.getOrNull()?.let {
                _loginSuccess.emit(true)
                responseLogin.postValue(true)
            } ?: responseLogin.postValue(true)
        }
    }

    fun setGoogleLoginResponse(response: ResponseLogin){
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                loginRepository.setGoogleLoginResponse(response)
            }.getOrNull()?.let {
                _loginSuccess.emit(true)
            } ?: _loginSuccess.emit(false)

        }
    }

    fun autoLogin() {
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                loginRepository.autoLogin()
            }.getOrNull()?.let {
                _loginSuccess.emit(true)
                responseLogin.postValue(true)
            } ?: responseLogin.postValue(true)
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch(Dispatchers.Main) {
            runCatching {
                loginRepository.resetPassword(email)
            }.getOrNull()?.let {
                _resetPasswordSuccess.emit(false) // ????????? ????????? ?????? ?????????, ?????????????????? ?????????
            } ?: _resetPasswordSuccess.emit(true)
        }
    }
}