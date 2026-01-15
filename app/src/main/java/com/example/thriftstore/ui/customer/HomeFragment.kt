package com.example.thriftstore.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.thriftstore.R
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.repository.CartRepository
import com.example.thriftstore.data.repository.CatalogRepository
import com.example.thriftstore.databinding.FragmentHomeBinding
import com.example.thriftstore.utils.SessionManager

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: CustomerViewModel by viewModels {
        val db = ThriftApp.database
        val catRepo = CatalogRepository(db.katalogDao())
        val cartRepo = CartRepository(db.cartDao(), db.orderDao())
        CustomerViewModelFactory(catRepo, cartRepo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        
        // Welcome message
        binding.tvHome.text = "Selamat Datang, ${sessionManager.getName() ?: "Tamu"}"

        val adapter = CustomerHomeAdapter { product ->
             val bundle = bundleOf("productId" to product.id)
             findNavController().navigate(R.id.action_customerHomeFragment_to_productDetailFragment, bundle)
        }

        binding.rvProducts.layoutManager = GridLayoutManager(context, 2)
        binding.rvProducts.adapter = adapter

        viewModel.allProducts.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }
}
