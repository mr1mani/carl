package com.example.mybook

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.MyAdapter
import com.example.mybook.MyItem
import com.example.mybook.R

class SellingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.selling_activity)

        // Set up the back button
        val backBtn = findViewById<ImageView>(R.id.backBtn)
        backBtn.setOnClickListener {
            onBackPressed() // This will navigate back
        }

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data for demonstration
        val itemList = mutableListOf<MyItem>()
        itemList.add(MyItem(R.drawable.logocolor, "Item 1", "$10.00", "2024-06-02"))
        itemList.add(MyItem(R.drawable.logo_black, "Item 2", "$15.00", "2024-06-03"))
        itemList.add(MyItem(R.drawable.logo_white, "Item 3", "$20.00", "2024-06-04"))
        itemList.add(MyItem(R.drawable.logo_white, "Item 3", "$20.00", "2024-06-04"))
        itemList.add(MyItem(R.drawable.logo_white, "Item 3", "$20.00", "2024-06-04"))
        itemList.add(MyItem(R.drawable.logo_white, "Item 3", "$20.00", "2024-06-04"))
        itemList.add(MyItem(R.drawable.logo_white, "Item 3", "$20.00", "2024-06-04"))


        // Set up RecyclerView adapter
        itemAdapter = MyAdapter(this, itemList)
        recyclerView.adapter = itemAdapter
    }
}
