package how.about.it.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import how.about.it.repository.ProfileRepository

class ProfileViewModel() : ViewModel() {

    val duplicateCheckNicknameSuccess = MutableLiveData<Boolean>()
    val duplicateCheckNicknameFailedMessage = MutableLiveData<String?>()

    private val profileRepository = ProfileRepository()

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
}