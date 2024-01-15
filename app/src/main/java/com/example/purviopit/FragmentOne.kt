package com.example.purviopit

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FragmentOne : Fragment(), OnItemClickListener {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var viewModel: SharedViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_one, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.productsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        productAdapter = ProductAdapter(emptyList(), this)
        recyclerView.adapter = productAdapter

        // Observe Products LiveData
        viewModel.getProducts().observe(viewLifecycleOwner, Observer { products ->
            productAdapter.updateProducts(products)
        })

        viewModel.loadProducts(requireContext())

        return view
    }

    override fun onItemClick(product: Product) {
        val imagePath = product.imagePath
        if (imagePath != null) {
            showDeleteConfirmationDialog(product, imagePath)
        }
    }

    private fun showDeleteConfirmationDialog(product: Product, imagePath: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_confirmation, null)
        val dialogImage = dialogView.findViewById<ImageView>(R.id.dialogImage)

        // Load the image from the local file path
        val file = File(imagePath)
        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            dialogImage.setImageBitmap(bitmap)
        }

        AlertDialog.Builder(context)
            .setView(dialogView)
            .setPositiveButton("Delete") { dialog, _ ->
                viewModel.deleteProduct(requireContext(), product)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
