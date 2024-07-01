package com.example.mybook


import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.mybook.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class RegisterActivity : AppCompatActivity() {

    // View binding
    private lateinit var binding: ActivityRegisterBinding

    // Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    // Progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Init progress dialog, will show while creating account| Register user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        // Handle back button click, go to the previous screen
        binding.backBtn.setOnClickListener{
            onBackPressed()
        }

        // Handle click, begin register
        binding.registerBtn.setOnClickListener{
            /*steps
            * 1) Input Data
            * 2) Validate Data
            * 3) Create Account - Firebase Auth
            * 4) Save User Info - Firebase Database*/
            validateData()
        }
    }

    private fun validateData() {
        // Input Data
        val name = binding.nameEt.text.toString().trim()
        val email = binding.emailEt.text.toString().trim()
        val password = binding.PasswordEt.text.toString().trim()
        val cPassword = binding.cpasswordEt.text.toString().trim()

        // Validate Data
        if (name.isEmpty()){
            // Empty name...
            Toast.makeText(this, "Enter your name...", Toast.LENGTH_SHORT).show()
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            // Invalid email pattern
            Toast.makeText(this, "Enter Email Pattern...", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            // Empty password
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show()
        }
        else if (cPassword.isEmpty()){
            // Empty password
            Toast.makeText(this, "Confirm password...", Toast.LENGTH_SHORT).show()
        }
        else if (password != cPassword){
            Toast.makeText(this, "Password doesn't match...", Toast.LENGTH_SHORT).show()
        }
        else{
            createUserAccount(name, email, password)
        }
    }

    private fun createUserAccount(name: String, email: String, password: String) {
        // Show progress
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Account created
                updateUserInfo(name, email)
            }
            .addOnFailureListener { e ->
                // Failed creating account
                progressDialog.dismiss()
                Toast.makeText(this, "Failed creating account due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUserInfo(name: String, email: String) {
        // Save User Info - Firebase Database
        progressDialog.setMessage("Saving user info...")

        val timestamp = System.currentTimeMillis()

        // Get current user uid, since the user is registered so we can get it now
        val uid = firebaseAuth.currentUser?.uid

        // Setup data to add in db
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["uid"] = uid!!
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["profileImage"] = "" // Add empty, will do in profile edit
        hashMap["userType"] = "user" // Possible values are user/admin, will change value to the admin manually on firebase db
        hashMap["timestamp"] = timestamp

        // Set data to db
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid)
            .setValue(hashMap)
            .addOnSuccessListener {
                // User info saved, open user dashboard
                progressDialog.dismiss()
                Toast.makeText(this, "Account Created...",Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@RegisterActivity, Store::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                // Failed adding data to db
                progressDialog.dismiss()
                Toast.makeText(this, "Failed saving user info  due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}
