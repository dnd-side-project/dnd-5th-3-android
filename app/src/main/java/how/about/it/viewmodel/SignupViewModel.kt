package how.about.it.viewmodel

import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {
    private var email=""
    private var password=""
    private var nickname=""

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
}