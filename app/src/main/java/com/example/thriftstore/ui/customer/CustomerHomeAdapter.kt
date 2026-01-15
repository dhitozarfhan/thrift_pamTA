package com.example.thriftstore.ui.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import coil.load
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thriftstore.R
import com.example.thriftstore.data.model.Katalog

class CustomerHomeAdapter(
    private val onItemClick: (Katalog) -> Unit
) : ListAdapter<Katalog, CustomerHomeAdapter.ProductViewHolder>(DiffCallback) {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvProdName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvProdPrice)
        val imgProduct: ImageView = itemView.findViewById(R.id.imgProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_customer, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        holder.tvName.text = product.name
        holder.tvPrice.text = "Rp ${product.price}"
        
        holder.imgProduct.load(product.image_url) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
            error(android.R.color.darker_gray)
        }
        
        holder.itemView.setOnClickListener { onItemClick(product) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Katalog>() {
        override fun areItemsTheSame(oldItem: Katalog, newItem: Katalog): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Katalog, newItem: Katalog): Boolean {
            return oldItem == newItem
        }
    }
}
