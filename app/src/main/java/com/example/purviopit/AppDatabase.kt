package com.example.purviopit

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Product::class], version = 1)
//анотация, която опредея класа като база данни в Room.
//entities е масив от класове/таблици, който в случая съдържа само един.
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
