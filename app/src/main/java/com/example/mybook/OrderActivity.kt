package com.example.mybook

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: MyAdapter
    private var orderItems: MutableList<MyItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.orders_activity)

        // Set up the back button
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed() // This will navigate back
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load order items from SharedPreferences
        loadOrderItemsFromSharedPreferences()

        // Set up RecyclerView adapter
        itemAdapter = MyAdapter(this, orderItems)
        recyclerView.adapter = itemAdapter
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
