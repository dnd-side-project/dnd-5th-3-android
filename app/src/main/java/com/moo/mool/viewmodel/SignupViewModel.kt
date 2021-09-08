package com.moo.mool.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moo.mool.model.RequestLogin
import com.moo.mool.model.RequestMember
import com.moo.mool.repository.LoginRepository
import com.moo.mool.repository.SignupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val loginRepository: LoginRepository
    , private val signupRepository: SignupRepository
) : ViewModel() {
    private var email = ""
    private var password = ""
    private var nickname = ""

    val signupSuccess = MutableLiveData<Boolean>()
    val signupFailedMessage = MutableLiveData<String?>()

    val duplicateCheckEmailSuccess = MutableLiveData<Boolean>()
    val duplicateCheckEmailFailedMessage = MutableLiveData<String?>()
    val duplicateCheckNicknameSuccess = MutableLiveData<Boolean>()
    val duplicateCheckNicknameFailedMessage = MutableLiveData<String?>()

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess = _loginSuccess.asStateFlow()
    val loginFailedMessage = MutableLiveData<String?>()

    private val _networkError = MutableStateFlow(false)
    val networkError = _networkError.asStateFlow()

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
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                loginRepository.loginUser(RequestLogin(getEmail(), getPassword()))
            }.getOrNull()?.let {
                _loginSuccess.emit(true)
            } ?: _networkError.emit(true)
        }
    }
}