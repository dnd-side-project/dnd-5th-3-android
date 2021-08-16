package how.about.it.viewmodel

import android.app.Application
import androidx.lifecycle.*
import how.about.it.database.TempPost
import how.about.it.database.TempPostDatabase
import how.about.it.repository.WriteRepository
import how.about.it.repository.TempPostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteViewModel(application: Application): AndroidViewModel(application) {

    val getAllTempList : LiveData<List<TempPost>>
    private var tempPostRepository : TempPostRepository

    val writeSuccess = MutableLiveData<Boolean>()
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

    fun uploadPost(title: String, content: String, productImageUrl: String?) {
        writeRepository.uploadPost(title, content, productImageUrl, object : WriteRepository.WriteCallBack {
            override fun onSuccess() {
                writeSuccess.postValue(true)
            }

            override fun onError(message: String?) {
                writeSuccess.postValue(false)
                writeFailedMessage.postValue(message)
            }
        })
    }
}