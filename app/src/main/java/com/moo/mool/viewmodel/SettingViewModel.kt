package com.moo.mool.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.moo.mool.repository.SettingRepository

class SettingViewModelFactory(val settingRepository: SettingRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(settingRepository::class.java).newInstance(settingRepository)
    }
}

class SettingViewModel(private val settingRepository: SettingRepository) : ViewModel() {

    val deleteAccountSuccess = MutableLiveData<Boolean>()
    val deleteAccountFailedMessage = MutableLiveData<String?>()

    fun deleteAccount() {
        settingRepository.deleteAccount(object : SettingRepository.SettingCallBack {
            override fun onSuccess() {
                deleteAccountSuccess.postValue(true)
            }
            override fun onError(message: String?) {
                deleteAccountSuccess.postValue(false)
                deleteAccountFailedMessage.postValue(message)
            }
        })
    }

    fun setHideEmojiMotion() = settingRepository.setHideEmojiMotion()
    fun setShowEmojiMotion() = settingRepository.setShowEmojiMotion()
    val isHideEmojiMotion = settingRepository.isHideEmojiMotion
}