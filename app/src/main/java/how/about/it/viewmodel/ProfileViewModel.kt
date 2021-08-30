package how.about.it.viewmodel

import android.app.Application
import androidx.lifecycle.*
import how.about.it.repository.ProfileRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val profileRepository = ProfileRepository(getApplication<Application>().applicationContext)

    val duplicateCheckNicknameSuccess = MutableLiveData<Boolean?>()
    val duplicateCheckNicknameFailedMessage = MutableLiveData<String?>()
    val updateNicknameSuccess = MutableLiveData<Boolean?>()
    val updateNicknameFailedMessage = MutableLiveData<String?>()

    val checkOldPasswordSuccess = MutableLiveData<Boolean?>()
    val checkOldPasswordFailedMessage = MutableLiveData<String?>()
    val checkNewPasswordSuccess = MutableLiveData<Boolean?>()
    val checkNewPasswordFailedMessage = MutableLiveData<String?>()

    var checkPasswordChanged = false

    val enableChange = combine(checkOldPasswordSuccess.asFlow(), checkNewPasswordSuccess.asFlow()) {
        checkOldPasswordSuccess, checkNewPasswordSuccess -> checkOldPasswordSuccess==true && checkNewPasswordSuccess==true
    }.onStart { emit(true) }.asLiveData()

    val updatePasswordSuccess = MutableLiveData<Boolean?>()
    val updatePasswordFailedMessage = MutableLiveData<String?>()

    fun duplicateCheckNickname(nickname: String) {
        profileRepository.duplicateCheckNickname(nickname, object : ProfileRepository.ProfileCallBack {
            override fun onSuccess() {
                duplicateCheckNicknameSuccess.postValue(true)
            }
            override fun onError(message: String?) {
                duplicateCheckNicknameSuccess.postValue(false)
                duplicateCheckNicknameFailedMessage.postValue(message)
            }
        })
    }

    fun updateNickname(nickname: String) {
        profileRepository.updateNickname(nickname, object : ProfileRepository.ProfileCallBack {
            override fun onSuccess() {
                updateNicknameSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                updateNicknameSuccess.postValue(false)
                updateNicknameFailedMessage.postValue(message)
            }
        })
    }

    fun updatePassword(newPassword: String) {
        profileRepository.updatePassword(newPassword, object : ProfileRepository.ProfileCallBack {
            override fun onSuccess() {
                updatePasswordSuccess.postValue(true)
                checkPasswordChanged = true
            }

            override fun onError(message: String?) {
                updatePasswordSuccess.postValue(false)
                updatePasswordFailedMessage.postValue(message)
            }
        })
    }

    fun checkOldPassword(password: String) {
        profileRepository.checkOldPassword(password, object : ProfileRepository.ProfileCallBack {
            override fun onSuccess() {
                checkOldPasswordSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                checkOldPasswordSuccess.postValue(false)
                checkOldPasswordFailedMessage.postValue(message)
            }
        })
    }

    fun checkNewPassword(password: String) {
        profileRepository.checkNewPassword(password, object : ProfileRepository.ProfileCallBack {
            override fun onSuccess() {
                checkNewPasswordSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                checkNewPasswordSuccess.postValue(false)
                checkNewPasswordFailedMessage.postValue(message)
            }
        })
    }
}