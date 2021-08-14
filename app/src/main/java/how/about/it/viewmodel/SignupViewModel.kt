package how.about.it.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import how.about.it.model.RequestMember
import how.about.it.repository.SignupRepository

class SignupViewModel() : ViewModel() {
    private var email = ""
    private var password = ""
    private var nickname = ""

    val signupSuccess = MutableLiveData<Boolean>()
    val signupFailedMessage = MutableLiveData<String?>()
    private val signupRepository = SignupRepository()

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
}