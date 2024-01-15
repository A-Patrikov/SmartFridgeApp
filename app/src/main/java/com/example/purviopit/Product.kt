package com.example.purviopit

import androidx.room.Entity
import androidx.room.PrimaryKey
// Използва се Room за управление на SQLite база данни в приложението.
@Entity(tableName = "products")
//анотация, която указва, че класът трябва да бъде използван като таблица в базата данни
data class Product(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val quantity: Int,
    val expiryDate: String,
    val imagePath: String?
)
