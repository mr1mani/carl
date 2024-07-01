package com.example.mybook

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mybook.R
import com.example.mybook.MyItem

class MyAdapter(private val context: Context, private var itemList: MutableList<MyItem>) :
    RecyclerView.Adapter<MyAdapter.ItemViewHolder>() {

    private var itemListFull: List<MyItem> = ArrayList(itemList)
    private var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_items, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.itemHeading.text = currentItem.heading
        holder.itemPrice.text = currentItem.price
        holder.itemDate.text = currentItem.date
        holder.itemImage.setImageResource(currentItem.imageResource)

        holder.itemView.setOnClickListener {
            listener?.onItemClick(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        val itemHeading: TextView = itemView.findViewById(R.id.item_heading)
        val itemPrice: TextView = itemView.findViewById(R.id.item_price)
        val itemDate: TextView = itemView.findViewById(R.id.item_date)
    }

    interface OnItemClickListener {
        fun onItemClick(item: MyItem)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // Filter method to filter the list based on the search query
    fun filter(query: String) {
        itemList = if (query.isEmpty()) {
            itemListFull.toMutableList()
        } else {
            itemListFull.filter {
                it.heading.contains(query, ignoreCase = true)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    fun isListEmpty(): Boolean {
        return itemList.isEmpty()
    }
}
