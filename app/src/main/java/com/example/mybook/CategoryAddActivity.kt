package com.example.mybook

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mybook.databinding.ActivityCategoryAddBinding
import com.example.mybook.databinding.ActivityDashboardAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CategoryAddActivity : AppCompatActivity() {

    //viewbinding
    private lateinit var binding: ActivityCategoryAddBinding

    // Firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    // Progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init //firebase auth
        firebaseAuth = FirebaseAuth.getInstance()


        //configure progress dailog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, go back

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }

        //handle click, begin upload category
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }

    private var category = ""

    private fun validateData() {
        //validate data

        //get fata
        category = binding.categoryEt.text.toString().trim()

        //validate data
        if (category.isEmpty()) {
            Toast.makeText(this, "Enter Category...", Toast.LENGTH_SHORT).show()
        } else {
            addCategoryFirebase()



        }
    }

    private fun addCategoryFirebase() {
      //show progress
        progressDialog.show()

        //get timestamp
        val timestamp = System.currentTimeMillis()

        //setup data to add in firebase db
        val hashMap = HashMap<String, Any>()
        hashMap["id"] = "$timestamp"
        hashMap["category"] = "category"
        hashMap["timestamp"] = "timestamp"
        hashMap["uid"] = "${firebaseAuth.uid}"

        //add to firebase db: Datebase Root > Category > categoryId > category info
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.child("$timestamp")
            .setValue(hashMap)
            .addOnSuccessListener {
                //added successfully
                progressDialog.dismiss()
                Toast.makeText(this, "Added sucessfully...", Toast.LENGTH_SHORT).show()


            }
            .addOnFailureListener{ e->
                //failed to add
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add due to $(e.message)...", Toast.LENGTH_SHORT).show()

            }



    }
}




