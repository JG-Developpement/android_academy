package com.jgdeveloppement.android_academie.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jgdeveloppement.android_academie.models.Bookmark

@Database(entities = [Bookmark::class], version = 1, exportSchema = false)
abstract class AcademyDatabase: RoomDatabase() {

    abstract fun bookmarkDao(): BookmarkDao

    companion object{

        @Volatile
        private var INSTANCE: AcademyDatabase? = null

        fun getDatabase(context: Context): AcademyDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AcademyDatabase::class.java,
                    "academy_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        fun clearAllData() {
            INSTANCE?.clearAllTables()
        }
    }
}