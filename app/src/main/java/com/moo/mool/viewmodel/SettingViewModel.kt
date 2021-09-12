package com.moo.mool.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.moo.mool.database.TempPost
import com.moo.mool.database.TempPostDatabase
import com.moo.mool.repository.SettingRepository
import com.moo.mool.repository.TempPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val settingRepository: SettingRepository,
    @ApplicationContext val context: Context
): ViewModel() {
    private var tempPostRepository : TempPostRepository

    val getAllTempList : LiveData<List<TempPost>>
    init {
        val tempPostDao = TempPostDatabase.getDatabase(context)!!.tempPostDao()
        tempPostRepository = TempPostRepository(tempPostDao)
        getAllTempList = tempPostRepository.getAllTempList
    }

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

    fun deleteTempPostAll() {
        viewModelScope.launch(Dispatchers.IO) {
            tempPostRepository.deleteAll()
        }
    }

    fun setHideEmojiMotion() = settingRepository.setHideEmojiMotion()
    fun setShowEmojiMotion() = settingRepository.setShowEmojiMotion()
    val isHideEmojiMotion = settingRepository.isHideEmojiMotion
}