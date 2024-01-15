package com.example.purviopit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

//Свързва данните за продуктите със съответния UI в RecyclerView.
class ProductAdapter(private var products: List<Product>, private val listener: OnItemClickListener) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {


    class ProductViewHolder(view: android.view.View) : RecyclerView.ViewHolder(view) {
        //съдържа UI TextView компонентите.
        val productName: TextView = view.findViewById(R.id.productName)
        val productQuantity: TextView = view.findViewById(R.id.productQuantity)
        val productExpiryDate: TextView = view.findViewById(R.id.productExpiryDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        //Създава нови ProductViewHolder елементи, които държат референции към UI TextView компонентите.
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        // Свързва данните за продуктите с определен ViewHolder.
        val product = products[position]
        holder.itemView.setOnClickListener {
            listener.onItemClick(product)
        }
        holder.productName.text = product.name
        holder.productQuantity.text = product.quantity.toString()
        holder.productExpiryDate.text = product.expiryDate
    }



    override fun getItemCount() = products.size

    // Method to update the products list
    @SuppressLint("NotifyDataSetChanged")
    fun updateProducts(newProducts: List<Product>) {
        products = newProducts
        notifyDataSetChanged()
    }
}
