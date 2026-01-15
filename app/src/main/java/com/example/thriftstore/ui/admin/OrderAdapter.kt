package com.example.thriftstore.ui.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.thriftstore.R
import com.example.thriftstore.data.model.Order
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrderAdapter(
    private val onItemClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(DiffCallback) {

    class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvOrderId: TextView = itemView.findViewById(R.id.tvOrderId)
        val tvDate: TextView = itemView.findViewById(R.id.tvOrderDate)
        val tvTotal: TextView = itemView.findViewById(R.id.tvOrderTotal)
        val tvStatus: TextView = itemView.findViewById(R.id.tvOrderStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = getItem(position)
        val date = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()).format(Date(order.order_date))
        
        holder.tvOrderId.text = "Order #${order.id}"
        holder.tvDate.text = date
        holder.tvTotal.text = "Rp ${order.total_amount}"
        holder.tvStatus.text = order.status.uppercase(Locale.getDefault())
        
        val statusBg = when (order.status.uppercase(Locale.getDefault())) {
            "COMPLETED", "SUCCESS" -> R.drawable.bg_status_success
            "PENDING" -> R.drawable.bg_status_pending
            "CANCELLED" -> R.drawable.bg_status_cancelled
            else -> R.drawable.bg_status_pending
        }
        holder.tvStatus.setBackgroundResource(statusBg)
        
        holder.itemView.setOnClickListener { onItemClick(order) }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}
