package com.example.purviopit

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.FileOutputStream
import java.util.Calendar
import java.util.UUID

class FragmentTwo : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private var imageBitmap: Bitmap? = null
    companion object {
        private const val CAMERA_REQUEST_CODE = 1001
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1002
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val view = inflater.inflate(R.layout.fragment_two, container, false)
        val nameEditText = view.findViewById<EditText>(R.id.editTextProductName)
        val quantityEditText = view.findViewById<EditText>(R.id.editTextQuantity)
        val expiryDateEditText = view.findViewById<EditText>(R.id.editTextExpiryDate)
        val saveButton = view.findViewById<Button>(R.id.buttonSaveProduct)
        val captureImageButton = view.findViewById<Button>(R.id.buttonCaptureImage)
        captureImageButton.setOnClickListener {
            requestCameraPermission()
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val quantity = quantityEditText.text.toString().toIntOrNull() ?: 0
            val expiryDate = expiryDateEditText.text.toString()

            // Check if the name is empty
            if (name.isEmpty()) {
                nameEditText.error = "Please enter a product name"
                return@setOnClickListener
            }

            // Check if the quantity is not a number or zero
            if ( quantity == 0) {
                quantityEditText.error = "Please enter a valid quantity"
                return@setOnClickListener
            }

            // Check if the expiry date is empty
            if (expiryDate.isEmpty()) {
                expiryDateEditText.error = "Please enter an expiry date"
                return@setOnClickListener
            }

            val imagePath = if (imageBitmap != null) saveImageToInternalStorage(imageBitmap!!) else ""
            val product = Product(name = name, quantity = quantity, expiryDate = expiryDate, imagePath = imagePath)
            viewModel.addProduct(requireContext(), product)
            Toast.makeText(requireContext(), "Product added successfully", Toast.LENGTH_SHORT).show()
        }

        expiryDateEditText.setOnClickListener {
            showDatePickerDialog()
        }

        return view
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "${selectedDay}/${selectedMonth + 1}/$selectedYear"
                view?.findViewById<EditText>(R.id.editTextExpiryDate)?.setText(selectedDate)
            }, year, month, day)

        datePickerDialog.show()
    }

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            openCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {
                    // Направи permission denial
                }
            }
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val bitmap = extras?.get("data")
            if (bitmap is Bitmap) {
                imageBitmap = bitmap
            }
        }
    }
    //This method is typically used for handling the result from a camera capture intent.
    //When the camera activity finishes, it returns a small Bitmap in the extras.


    private fun saveImageToInternalStorage(bitmap: Bitmap): String {
        val contextWrapper = ContextWrapper(requireContext())
        val directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val file = File(directory, "${UUID.randomUUID()}.jpg")

        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }

        return file.absolutePath
    }
}