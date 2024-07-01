package com.example.mybook

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SellerEditorActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var itemNameEditText: EditText
    private lateinit var itemPriceEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var itemDescriptionEditText: EditText
    private var selectedImageUri: Uri? = null
    private var imageBitmap: Bitmap? = null
    private val GALLERY_REQUEST_CODE = 1001

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selleditor_activity)

        imageView = findViewById(R.id.image_view)
        itemNameEditText = findViewById(R.id.item_name_edit_text)
        itemPriceEditText = findViewById(R.id.item_price_edit_text)
        addressEditText = findViewById(R.id.add_edit_text)
        itemDescriptionEditText = findViewById(R.id.item_description_edit_text)

        val cameraText = findViewById<TextView>(R.id.camera_text)
        cameraText.setOnClickListener {
            openCamera()
        }

        val uploadImagesText = findViewById<TextView>(R.id.upload_images_text)
        uploadImagesText.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
        }

        val submitText = findViewById<TextView>(R.id.submit_text)
        submitText.setOnClickListener {
            submitData()
        }

        val exitText = findViewById<TextView>(R.id.exit_text)
        exitText.setOnClickListener {
            onBackPressed()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (cameraIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            // Handle error: No camera app found
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        } else if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            selectedImageUri?.let {
                imageView.setImageURI(it)
            }
        }
    }

    private fun submitData() {
        val itemName = itemNameEditText.text.toString()
        val itemPrice = itemPriceEditText.text.toString()
        val address = addressEditText.text.toString()
        val itemDescription = itemDescriptionEditText.text.toString()

        if (itemName.isEmpty() || itemPrice.isEmpty() || address.isEmpty() || itemDescription.isEmpty() || (selectedImageUri == null && imageBitmap == null)) {
            Toast.makeText(this, "Please fill in all fields and select an image.", Toast.LENGTH_SHORT).show()
            return
        }

        // Here you can add the code to save the data to your storage
        // For example, save to a local database or send to a remote server

        // Example toast to indicate success
        Toast.makeText(this, "Data submitted successfully", Toast.LENGTH_SHORT).show()
    }
}
