package com.example.mybook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CartActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: MyAdapter
    private lateinit var checkoutButton: Button
    private lateinit var totalPriceTextView: TextView
    private var cartItems: MutableList<MyItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_activity)

        // Set up the back button
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed() // This will navigate back
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up Checkout button
        checkoutButton = findViewById(R.id.checkoutButton)
        checkoutButton.setOnClickListener {
            // Handle checkout logic here
            showToast("Checkout button clicked")
        }

        // Set up totalPriceTextView
        totalPriceTextView = findViewById(R.id.totalPriceTextView)

        // Load cart items from SharedPreferences
        loadCartItemsFromSharedPreferences()

        // Set up RecyclerView adapter
        itemAdapter = MyAdapter(this, cartItems)
        recyclerView.adapter = itemAdapter

        // Calculate and display total price
        displayTotalPrice()
    }

    private fun loadCartItemsFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("CartItems", Context.MODE_PRIVATE)
        val cartItemsJson = sharedPreferences.getString("cartItemsJson", null)

        if (!cartItemsJson.isNullOrEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<List<MyItem>>() {}.type
            cartItems = gson.fromJson(cartItemsJson, type)
        }
    }

    private fun displayTotalPrice() {
        var totalPrice = 0.0
        for (item in cartItems) {
            try {
                val priceString = item.price.replace("Rs.", "").replace(",", "")
                totalPrice += priceString.toDouble() // Assuming price is a String
            }
            catch (e: NullPointerException) {
                e.printStackTrace()
                showToast("Null item found in cart")
            }
        }
        totalPriceTextView.text = "Total Price: $${String.format("%.2f", totalPrice)}"
    }



    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
