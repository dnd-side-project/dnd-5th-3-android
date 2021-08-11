package how.about.it.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TempPostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tempPost: TempPost)

    @Update
    fun update(tempPost: TempPost)

    @Delete
    fun deleteTempList(tempPost: TempPost)

    @Query("SELECT * FROM TempPost")
    fun getAllTempList() : LiveData<List<TempPost>>
}