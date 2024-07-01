package com.example.mybook

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mybook.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LoginActivity : AppCompatActivity() {

    //viewbinding
    private lateinit var binding: ActivityLoginBinding

    // Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    // Progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // Init progress dialog, will show while creating account| Register user
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, not have account . go to register screen
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))

        }
        //handle click, begin login
        binding.loginBtn.setOnClickListener {
            /*steps
            * 1) Input Data
            * 2) Validate Data
            * 3) Login - Firebase Auth
            * 4) Check user type - Firebase Database
            * 5)if admin- move to admin dashboard */
            validateData()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Always navigate to MainActivity
        val mainIntent = Intent(this, MainActivity::class.java)
        startActivity(mainIntent)
        finish()  // Close the current activity
    }


    private var email = ""
    private var password = ""

    private fun validateData() {
        //1) Input Data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //2) Validate Data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format...", Toast.LENGTH_SHORT).show()

        } else if (password.isEmpty()) {
            // Empty password
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show()
        } else {
            loginUser()
        }


    }

    private fun loginUser() {
        //3) Login - Firebase Auth

        //show progress
        progressDialog.setMessage("Logging In...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // login success
                checkUser()
            }
            .addOnFailureListener { e ->
                // login failed
                progressDialog.dismiss()
                Toast.makeText(this, "login failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun checkUser() {
        progressDialog.setMessage("Checking User...")

        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()

                    // Get user type
                    val userType = snapshot.child("userType").value
                    if (userType == "user") {
                        // Regular user, open user dashboard
                        startActivity(Intent(this@LoginActivity, Store::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle cancellation
                }
            })
    }

}
