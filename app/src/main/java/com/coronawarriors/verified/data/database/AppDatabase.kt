package com.coronawarriors.verified.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.coronawarriors.verified.data.database.dao.ContactDao
import com.coronawarriors.verified.data.database.dao.RequirementsDao
import com.coronawarriors.verified.data.database.entity.Contact
import com.coronawarriors.verified.data.database.entity.Requirements

//@Database(entities = [Requirements::class, Contact::class],
//    version = 1, exportSchema = false)
abstract class AppDatabase()/*: RoomDatabase()*/ {

    abstract fun requirementDao(): RequirementsDao

    abstract fun contactDao(): ContactDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

//        fun getDatabase(context: Context): AppDatabase{
//            val tempInstance = INSTANCE
//            if (tempInstance != null) {
//                return tempInstance
//            }
//            synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "cache_db"
//                ).build()
//
//                INSTANCE = instance
//                return instance
//            }
//        }

    }
}