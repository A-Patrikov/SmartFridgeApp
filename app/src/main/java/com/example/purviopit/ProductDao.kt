package com.example.purviopit

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Product>
//    fun getAllProducts(): LiveData<List<Product>>

    @Insert
    fun insertProduct(product: Product)
    @Delete
    fun deleteProduct(product: Product)

    @Query("SELECT * FROM products WHERE expiryDate <= :thresholdDate")
    fun getExpiringProducts(thresholdDate: String): List<Product>

}

