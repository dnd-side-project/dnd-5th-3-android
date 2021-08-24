package how.about.it.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import how.about.it.repository.ProfileRepository
class ProfileViewModelFactory(val profileRepository: ProfileRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(profileRepository::class.java)
            .newInstance(profileRepository)
    }
}

class ProfileViewModel(private val profileRepository: ProfileRepository) : ViewModel() {

    val duplicateCheckNicknameSuccess = MutableLiveData<Boolean?>()
    val duplicateCheckNicknameFailedMessage = MutableLiveData<String?>()
    val updateNicknameSuccess = MutableLiveData<Boolean?>()
    val updateNicknameFailedMessage = MutableLiveData<String?>()

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
}