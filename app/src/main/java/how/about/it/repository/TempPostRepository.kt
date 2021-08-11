package how.about.it.repository

import androidx.lifecycle.LiveData
import how.about.it.database.TempPost
import how.about.it.database.TempPostDao

class TempPostRepository(private val tempPostDao : TempPostDao) {
    val getAllTempList : LiveData<List<TempPost>> = tempPostDao.getAllTempList()

    fun insert(tempPost: TempPost){
        tempPostDao.insert(tempPost)
    }
    fun update(tempPost: TempPost){
        tempPostDao.update(tempPost)
    }
    fun deleteTempList(tempPost: TempPost){
        tempPostDao.deleteTempList(tempPost)
    }
}