package com.robybp.mytransferapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.robybp.mytransferapp.dao.GuestBookDao
import com.robybp.mytransferapp.models.datamodels.Driver
import com.robybp.mytransferapp.models.datamodels.Guest

@Database(entities = [Guest::class, Driver::class], version = 1, exportSchema = false)
abstract class GuestBookDatabase: RoomDatabase() {

    abstract fun guestBookDao(): GuestBookDao

    companion object {

        @Volatile
        private var INSTANCE : GuestBookDatabase? = null

        fun getDatabase(context: Context) : GuestBookDatabase? {
            val tempInstance = INSTANCE
            if (INSTANCE != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GuestBookDatabase::class.java,
                    "user_task_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
