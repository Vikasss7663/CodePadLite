package com.coding.codepadlite.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Code::class], version = 2)
abstract class CodeDatabase: RoomDatabase() {

    abstract fun codeDao(): CodeDao

    companion object {

        @Volatile
        private var INSTANCE: CodeDatabase? = null

        fun getDatabase(context: Context): CodeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext,
                    CodeDatabase::class.java, "code-database")
                    .fallbackToDestructiveMigration() // data is lost
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

}