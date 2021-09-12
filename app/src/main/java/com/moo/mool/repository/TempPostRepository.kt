package com.moo.mool.repository

import androidx.lifecycle.LiveData
import com.moo.mool.database.TempPost
import com.moo.mool.database.TempPostDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

    fun deleteAll() {
        CoroutineScope(Dispatchers.IO).launch {
            tempPostDao.deleteAll()
        }
    }
}