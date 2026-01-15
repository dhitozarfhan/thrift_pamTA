package com.example.thriftstore.ui.admin

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
import com.google.android.material.button.MaterialButton

class CatalogAdapter(
    private val onEditClick: (Katalog) -> Unit,
    private val onDeleteClick: (Katalog) -> Unit
) : ListAdapter<Katalog, CatalogAdapter.CatalogViewHolder>(DiffCallback) {

    class CatalogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvItemName)
        val tvPrice: TextView = itemView.findViewById(R.id.tvItemPrice)
        val imgPreview: ImageView = itemView.findViewById(R.id.imgAdminPreview)
        val btnEdit: MaterialButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_catalog_admin, parent, false)
        return CatalogViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogViewHolder, position: Int) {
        val currenthelper = getItem(position)
        holder.tvName.text = currenthelper.name
        holder.tvPrice.text = "Rp ${currenthelper.price}"
        
        holder.imgPreview.load(currenthelper.image_url) {
            crossfade(true)
            placeholder(android.R.color.darker_gray)
            error(android.R.color.darker_gray)
        }
        // holder.tvStock.text = "Stock: ${currenthelper.stock}"
        
        holder.btnEdit.setOnClickListener { onEditClick(currenthelper) }
        holder.btnDelete.setOnClickListener { onDeleteClick(currenthelper) }
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
