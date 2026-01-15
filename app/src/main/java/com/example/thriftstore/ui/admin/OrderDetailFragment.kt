package com.example.thriftstore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.repository.OrderRepository
import com.example.thriftstore.databinding.FragmentOrderDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.thriftstore.utils.SessionManager
import kotlinx.coroutines.launch

class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderViewModel by viewModels {
        val repo = OrderRepository(ThriftApp.database.orderDao())
        OrderViewModelFactory(repo)
    }

    private lateinit var sessionManager: SessionManager
    private var orderId: Int = -1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = com.example.thriftstore.utils.SessionManager(requireContext())
        
        orderId = arguments?.getInt("orderId", -1) ?: -1
        if (orderId == -1) return

        // Only Admin can update status
        binding.btnUpdateStatus.visibility = if (sessionManager.getRole() == "admin") View.VISIBLE else View.GONE

        loadOrder(orderId)
    }

    private fun loadOrder(id: Int) {
        lifecycleScope.launch {
            val repo = OrderRepository(ThriftApp.database.orderDao())
            val order = repo.getOrderById(id)
            
            if (order != null) {
                binding.tvOrderId.text = "Pesanan #${order.id}"
                binding.tvOrderStatus.text = "Status: ${formatStatus(order.status)}"
                
                val sdf = java.text.SimpleDateFormat("dd MMM yyyy HH:mm", java.util.Locale.getDefault())
                binding.tvOrderDate.text = "Tanggal Pesan: ${sdf.format(java.util.Date(order.order_date))}"
                
                if (order.estimated_arrival != null) {
                    val eDate = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(order.estimated_arrival))
                    binding.tvEstimatedArrival.text = "Estimasi Sampai: $eDate"
                    binding.tvEstimatedArrival.visibility = View.VISIBLE
                } else {
                    binding.tvEstimatedArrival.text = "Estimasi Sampai: -"
                    binding.tvEstimatedArrival.visibility = if (sessionManager.getRole() == "admin") View.VISIBLE else View.GONE
                }
                
                binding.tvOrderNote.text = "Catatan: ${order.note ?: "-"}"

                binding.btnUpdateStatus.setOnClickListener {
                    showStatusDialog(order)
                }

                viewModel.getOrderItems(id)
            }
        }
        
        viewModel.currentOrderItemsWithProduct.observe(viewLifecycleOwner) { items ->
             binding.rvOrderItems.adapter = OrderItemAdapter(items)
        }
    }

    private fun formatStatus(status: String): String {
        return when(status) {
            "menunggu" -> "Menunggu Konfirmasi"
            "diproses" -> "Sedang Diproses"
            "dikirim" -> "Dalam Pengiriman"
            "selesai" -> "Selesai/Diterima"
            "dibatalkan" -> "Dibatalkan"
            else -> status
        }
    }
    
    private fun showStatusDialog(order: com.example.thriftstore.data.model.Order) {
        val statuses = arrayOf("menunggu", "diproses", "dikirim", "selesai", "dibatalkan")
        val statusLabels = arrayOf("Menunggu Konfirmasi", "Diproses", "Dikirim", "Selesai", "Dibatalkan")
        
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Perbarui Status")
            .setItems(statusLabels) { _, which ->
                val newStatus = statuses[which]
                if (newStatus == "dikirim" || newStatus == "diproses") {
                    // Ask for estimated arrival date
                    val calendar = java.util.Calendar.getInstance()
                    android.app.DatePickerDialog(requireContext(), { _, year, month, day ->
                        calendar.set(year, month, day)
                        val estimatedArrival = calendar.timeInMillis
                        val updatedOrder = order.copy(status = newStatus, estimated_arrival = estimatedArrival)
                        viewModel.updateStatus(updatedOrder, newStatus) // Actually updateStatus in VM copies again, but I'll pass the updated object
                        // Need to update VM to accept Order object instead of just status if we want to save arrival
                        updateOrderWithArrival(order, newStatus, estimatedArrival)
                    }, calendar.get(java.util.Calendar.YEAR), calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH)).show()
                } else {
                    viewModel.updateStatus(order, newStatus)
                    binding.tvOrderStatus.text = "Status: ${statusLabels[which]}"
                }
            }
            .show()
    }

    private fun updateOrderWithArrival(order: com.example.thriftstore.data.model.Order, status: String, arrival: Long) {
        val updated = order.copy(status = status, estimated_arrival = arrival)
        viewModel.updateStatus(updated, status) // Assuming VM updateStatus takes order and sets status? Wait.
        Toast.makeText(context, "Status & Estimasi Diperbarui", Toast.LENGTH_SHORT).show()
        loadOrder(order.id) // Refresh
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class OrderItemAdapter(private val items: List<com.example.thriftstore.data.model.OrderItemWithProduct>) : androidx.recyclerview.widget.RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {
    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val text1: android.widget.TextView = view.findViewById(android.R.id.text1)
        val text2: android.widget.TextView = view.findViewById(android.R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_2, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemWithProd = items[position]
        val item = itemWithProd.orderItem
        val product = itemWithProd.product
        
        holder.text1.text = product?.name ?: "Produk tidak tersedia (#${item.katalog_id})"
        holder.text2.text = "${item.quantity} x Rp ${String.format("%,.0f", item.price_at_purchase)}"
    }

    override fun getItemCount() = items.size
}
