package com.example.purviopit

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import kotlin.concurrent.thread

class SharedViewModel : ViewModel() {
    private val products = MutableLiveData<List<Product>>()

    fun getProducts(): LiveData<List<Product>> = products

    fun loadProducts(context: Context) {
        thread {
            val productList = DatabaseClient.getDatabase(context).productDao().getAllProducts()
            products.postValue(productList)
        }
    }

    fun addProduct(context: Context, product: Product) {
        Thread {
            DatabaseClient.getDatabase(context).productDao().insertProduct(product)
            loadProducts(context)  // Make sure this method updates the LiveData
        }.start()
    }

    fun deleteProduct(context: Context, product: Product) {
        thread {
            DatabaseClient.getDatabase(context).productDao().deleteProduct(product)
            loadProducts(context)  // Refresh the product list
        }
    }

}
