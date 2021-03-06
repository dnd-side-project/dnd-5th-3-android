package com.moo.mool.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.moo.mool.database.TempPost
import com.moo.mool.database.TempPostDatabase
import com.moo.mool.repository.WriteRepository
import com.moo.mool.repository.TempPostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class WriteViewModel(application: Application): AndroidViewModel(application) {

    val getAllTempList : LiveData<List<TempPost>>
    private var tempPostRepository : TempPostRepository

    val writeSuccess = MutableLiveData<Boolean>()
    val writePostId = MutableLiveData<String>()
    val writeFailedMessage = MutableLiveData<String?>()
    private val writeRepository = WriteRepository(application.applicationContext)

    init {
        val tempPostDao = TempPostDatabase.getDatabase(application)!!.tempPostDao()
        tempPostRepository = TempPostRepository(tempPostDao)
        getAllTempList = tempPostRepository.getAllTempList
    }
    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WriteViewModel(application) as T
        }
    }

    fun addTempPost(tempPost: TempPost) {
        viewModelScope.launch(Dispatchers.IO) {
            tempPostRepository.insert(tempPost)
        }
    }
    fun deleteTempPost(tempPost: TempPost) {
        viewModelScope.launch(Dispatchers.IO) {
            tempPostRepository.deleteTempList(tempPost)
        }
    }

    fun uploadPost(title: String, content: String, file: File?) {
        writeRepository.uploadPost(title, content, file, object : WriteRepository.WriteCallBack {
            override fun onSuccess(postId: String) {
                writePostId.postValue(postId)
                writeSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                writeSuccess.postValue(false)
                writeFailedMessage.postValue(message)
            }
        })
    }
}