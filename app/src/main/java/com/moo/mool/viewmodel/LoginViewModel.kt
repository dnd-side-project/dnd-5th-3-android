package com.moo.mool.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.model.RequestLogin
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
                _resetPasswordSuccess.emit(false) // 별도의 응답이 오는 경우는, 오류메시지의 경우임
            } ?: _resetPasswordSuccess.emit(true)
        }
    }
}