package com.example.thriftstore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.repository.OrderRepository
import com.example.thriftstore.databinding.FragmentSalesReportBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import androidx.core.os.bundleOf
import com.example.thriftstore.R

class SalesReportFragment : Fragment() {

    private var _binding: FragmentSalesReportBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderViewModel by viewModels {
        val repo = OrderRepository(ThriftApp.database.orderDao())
        OrderViewModelFactory(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSalesReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.totalRevenue.observe(viewLifecycleOwner) { revenue ->
            val total = revenue ?: 0.0
            binding.tvTotalRevenue.text = String.format(java.util.Locale("id", "ID"), "Rp %,.0f", total)
        }

        viewModel.totalSalesCount.observe(viewLifecycleOwner) { count ->
            val total = count ?: 0
            binding.tvTotalTransactions.text = "$total"
        }

        val adapter = OrderAdapter { order ->
            try {
                val bundle = bundleOf("orderId" to order.id)
                findNavController().navigate(R.id.action_salesReportFragment_to_orderDetailFragment, bundle)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        
        binding.rvRecentSales.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        viewModel.completedOrders.observe(viewLifecycleOwner) { orders ->
            if (orders != null) {
                adapter.submitList(orders)
                binding.tvRecentSalesLabel.visibility = if (orders.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
