package com.example.purviopit

import android.content.Context
import androidx.room.Room

object DatabaseClient {
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabase::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "MyDatabase.db"
                ).build()
            }
        }
        return instance!!
    }
}
