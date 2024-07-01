package com.example.mybook

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import com.example.mybook.databinding.ActivityStoreBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Store : AppCompatActivity(), MyAdapter.OnItemClickListener {
    private lateinit var binding: ActivityStoreBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up RecyclerView adapter
        val itemList = mutableListOf<MyItem>()
        itemList.add(MyItem(R.drawable.highlander, "Highlander", "Rs.3,00,00,000.00", "Islamabad"))
        itemList.add(MyItem(R.drawable.m2_coupe, "M2 Coupe", "Rs.4,00,00,000.00", "Peshawar"))
        itemList.add(MyItem(R.drawable.xm_suv, "XM SUV", "Rs.5,00,00,000.00", "DHA"))

        itemAdapter = MyAdapter(this, itemList)
        recyclerView.adapter = itemAdapter

        itemAdapter.setOnItemClickListener(this)

        val cameraFab = findViewById<FloatingActionButton>(R.id.camera)
        cameraFab.setOnClickListener {
            val intent = Intent(this, SellerEditorActivity::class.java)
            startActivity(intent)
        }

        // Set up bottom navigation view
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_store -> {
                    // Already in Store, do nothing
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left)
                    true
                }
                else -> false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_store

        // Set up search view
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                itemAdapter.filter(newText.orEmpty())
                return true
            }
        })
    }

    override fun onItemClick(item: MyItem) {
        // Navigate to DetailActivity and pass item details as extras
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("heading", item.heading)
            putExtra("price", item.price)
            putExtra("Date", item.date)
            putExtra("imageResource", item.imageResource)
        }
        startActivity(intent)
    }
}
