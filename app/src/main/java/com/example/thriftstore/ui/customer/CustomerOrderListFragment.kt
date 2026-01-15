package com.example.thriftstore.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thriftstore.R
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.repository.OrderRepository
import com.example.thriftstore.databinding.FragmentOrderListBinding
import com.example.thriftstore.ui.admin.OrderAdapter
import com.example.thriftstore.ui.admin.OrderViewModel
import com.example.thriftstore.ui.admin.OrderViewModelFactory
import com.example.thriftstore.utils.SessionManager

class CustomerOrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null // Reuse Admin Layout, it's just a recycler view
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: OrderViewModel by viewModels {
        val repo = OrderRepository(ThriftApp.database.orderDao())
        OrderViewModelFactory(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        val adapter = OrderAdapter { order ->
            val bundle = bundleOf("orderId" to order.id)
            findNavController().navigate(R.id.action_customerOrderListFragment_to_orderDetailFragment, bundle)
        }

        binding.rvOrders.layoutManager = LinearLayoutManager(context)
        binding.rvOrders.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        binding.rvOrders.adapter = adapter

        if (userId != -1) {
            viewModel.getOrdersByUser(userId).observe(viewLifecycleOwner) { list ->
                adapter.submitList(list)
                binding.tvEmptyOrders.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
