package com.example.mybook

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mybook.databinding.ActivityDetailBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var cartItems: MutableList<MyItem> = mutableListOf()
    private var orderItems: MutableList<MyItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data from intent
        val heading = intent.getStringExtra("heading") ?: ""
        val price = intent.getStringExtra("price") ?: ""
        val date = intent.getStringExtra("Date") ?: ""
        val imageResource = intent.getIntExtra("imageResource", 0)

        // Set data to views
        binding.itemImageView.setImageResource(imageResource)
        binding.itemHeadingTextView.text = heading
        binding.itemPriceTextView.text = price
        binding.addEditText.text = date
        binding.itemDescriptionTextView.text = "Some description"

        // Handle click on cart icon
        binding.cartText.setOnClickListener {
            val item = MyItem(imageResource, heading, price, date)
            addToCart(item)
            Toast.makeText(this, "Item added to cart", Toast.LENGTH_SHORT).show()
        }

        // Handle click on buy button
        binding.buyText.setOnClickListener {
            val item = MyItem(imageResource, heading, price, date)
            addToOrder(item)
            Toast.makeText(this, "Item added to orders", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        // Handle click on exit button
        binding.exitText.setOnClickListener {
            finish()
        }

        // Load cart items from SharedPreferences
        loadCartItemsFromSharedPreferences()

        // Load order items from SharedPreferences
        loadOrderItemsFromSharedPreferences()
    }

    private fun addToCart(item: MyItem) {
        cartItems.add(item)
        saveCartItemsToSharedPreferences()
    }

    private fun addToOrder(item: MyItem) {
        orderItems.add(item)
        saveOrderItemsToSharedPreferences()
    }

    private fun saveCartItemsToSharedPreferences() {
        val gson = Gson()
        val cartItemsJson = gson.toJson(cartItems)

        val sharedPreferences = getSharedPreferences("CartItems", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("cartItemsJson", cartItemsJson)
        editor.apply()
    }

    private fun saveOrderItemsToSharedPreferences() {
        val gson = Gson()
        val orderItemsJson = gson.toJson(orderItems)

        val sharedPreferences = getSharedPreferences("OrderItems", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("orderItemsJson", orderItemsJson)
        editor.apply()
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

    private fun loadOrderItemsFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences("OrderItems", Context.MODE_PRIVATE)
        val orderItemsJson = sharedPreferences.getString("orderItemsJson", null)

        if (!orderItemsJson.isNullOrEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<List<MyItem>>() {}.type
            orderItems = gson.fromJson(orderItemsJson, type)
        }
    }
}
