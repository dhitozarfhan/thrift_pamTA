package com.example.thriftstore.ui.customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import coil.load
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thriftstore.R
import com.example.thriftstore.data.model.CartItem
import com.example.thriftstore.data.model.CartItemWithProduct

class CartAdapter(
    private val onRemoveClick: (CartItem) -> Unit
) : ListAdapter<CartItemWithProduct, CartAdapter.CartViewHolder>(DiffCallback) {

    class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCartItemName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvCartItemPrice)
        val tvQty: TextView = itemView.findViewById(R.id.tvCartItemQty)
        val imgItem: ImageView = itemView.findViewById(R.id.imgCartItem)
        val btnRemove: ImageButton = itemView.findViewById(R.id.btnRemoveCartItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val itemWithProduct = getItem(position)
        val item = itemWithProduct.cartItem
        val product = itemWithProduct.product
        
        holder.tvName.text = product.name
        holder.tvPrice.text = "Rp ${product.price}"
        holder.tvQty.text = "Qty: ${item.quantity}"
        
        holder.imgItem.load(product.image_url) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
            error(android.R.color.darker_gray)
        }
        
        holder.btnRemove.setOnClickListener { onRemoveClick(item) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CartItemWithProduct>() {
        override fun areItemsTheSame(oldItem: CartItemWithProduct, newItem: CartItemWithProduct): Boolean {
            return oldItem.cartItem.id == newItem.cartItem.id
        }

        override fun areContentsTheSame(oldItem: CartItemWithProduct, newItem: CartItemWithProduct): Boolean {
            return oldItem == newItem
        }
    }
}
