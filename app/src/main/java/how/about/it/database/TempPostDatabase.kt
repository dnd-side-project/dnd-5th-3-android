package how.about.it.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TempPost::class], version = 1)
abstract class TempPostDatabase : RoomDatabase() {
    abstract fun tempPostDao() : TempPostDao

    companion object{
        @Volatile
        private var instance : TempPostDatabase?= null

        @Synchronized
        fun getDatabase(context: Context) : TempPostDatabase? {
            if(instance == null) {
                synchronized(TempPostDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TempPostDatabase::class.java,
                        "tempPost.db"
                    ).build()
                }
            }
            return instance
        }
    }
}