package com.example.mybook

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

data class User(
    val email: String? = null,
    val name: String? = null,
    val profileImage: String? = null,
    val timestamp: Long? = null,
    val uid: String? = null,
    val userType: String? = null
)

class ProfileActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private val GALLERY_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        setContentView(R.layout.activity_profile)

        val cartTextView: TextView = findViewById(R.id.cart_text)
        val ordersTextView: TextView = findViewById(R.id.orders_text)
        val sellingTextView: TextView = findViewById(R.id.selling_text)

        val cameraFab = findViewById<FloatingActionButton>(R.id.camera)
        cameraFab.setOnClickListener {
            val intent = Intent(this, SellerEditorActivity::class.java)
            startActivity(intent)
        }
        val profilePicture = findViewById<ImageView>(R.id.profile_picture)
        profilePicture.setOnClickListener {
            openGallery()
        }
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_profile -> {
                    // Already in Store, do nothing
                    true
                }
                R.id.navigation_store -> {
                    val intent = Intent(this, Store::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_profile

        // Set OnClickListener for Cart TextView
        cartTextView.setOnClickListener {
            // Start CartActivity
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for Orders TextView
        ordersTextView.setOnClickListener {
            // Start OrdersActivity
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        // Set OnClickListener for Selling TextView
        sellingTextView.setOnClickListener {
            // Start SellingActivity
            val intent = Intent(this, SellingActivity::class.java)
            startActivity(intent)
        }
        // Check if the user is logged in
        if (firebaseAuth.currentUser == null) {
            // Show a toast message
            Toast.makeText(this, "You need to log in to access the profile.", Toast.LENGTH_SHORT).show()

            // Redirect to LoginActivity if not logged in
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish()  // Close the current activity
            return
        }

        // Display email and username
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            val userEmail = firebaseUser.email
            val userId = firebaseUser.uid
            findViewById<TextView>(R.id.email_text).text = "Email: $userEmail"

            // Retrieve user data from Firebase Realtime Database
            databaseReference.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        val name = user.name
                        findViewById<TextView>(R.id.username_text).text = "Username: $name"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Toast.makeText(applicationContext, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                }
            })
        }
        // Set up BottomNavigationView, logout functionality, and camera button
        val logoutTextView = findViewById<TextView>(R.id.textView2)
        logoutTextView.setOnClickListener {
            logout()
        }

    }

    private fun logout() {
        // Clear the login status
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLoggedIn", false).apply()

        // Redirect to LoginActivity
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish()  // Close the current activity
    }

    override fun onBackPressed() {
        // Check if the user is logged in
        if (firebaseAuth.currentUser == null) {
            // If logged out, navigate to MainActivity
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
            finish()  // Close the current activity
        } else {
            // If logged in, proceed with the default back button behavior
            super.onBackPressed()
        }
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri = data.data
            // Update profile picture with the selected image
            findViewById<ImageView>(R.id.profile_picture).setImageURI(selectedImageUri)
            // Save the URI in SharedPreferences
            val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            sharedPreferences.edit().putString("profileImageUri", selectedImageUri.toString()).apply()
        }
    }

    private fun loadProfilePicture() {
        // Load the profile image URI from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
        val profileImageUri = sharedPreferences.getString("profileImageUri", null)
        // If a custom profile image URI is available, load and display it
        if (profileImageUri != null) {
            findViewById<ImageView>(R.id.profile_picture).setImageURI(Uri.parse(profileImageUri))
        } else {
            // If no custom profile image is available, display the default profile image
            findViewById<ImageView>(R.id.profile_picture).setImageResource(R.drawable.ic_person_gray)
        }
    }
}
