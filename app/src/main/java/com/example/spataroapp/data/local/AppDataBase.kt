package com.example.spataroapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.spataroapp.data.entities.Profile
import com.example.spataroapp.data.entities.User
import com.example.spataroapp.data.entities.UserProfile

@Database(entities = [User::class, Profile::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun appDataBaseDao(): AppDataBaseDao

    companion object{
        @Volatile private var instance: AppDataBase? = null
        //Get the DB
        fun getDatabase(context: Context): AppDataBase{
            val tempInstance = instance
            if(tempInstance!=null){
                return tempInstance
            }
            synchronized(this){
                instance = buildDatabase(context)
                return instance as AppDataBase
            }
        }
        //Build the DB
        private fun buildDatabase(appContext: Context):AppDataBase {
            return Room.databaseBuilder(appContext.applicationContext, AppDataBase::class.java, "spataro")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}