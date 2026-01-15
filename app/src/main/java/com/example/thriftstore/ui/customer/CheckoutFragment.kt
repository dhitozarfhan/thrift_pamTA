package com.example.thriftstore.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thriftstore.R
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.model.CartItem
import com.example.thriftstore.data.model.CartItemWithProduct
import com.example.thriftstore.data.model.Katalog
import com.example.thriftstore.data.repository.CartRepository
import com.example.thriftstore.data.repository.CatalogRepository
import com.example.thriftstore.databinding.FragmentCheckoutBinding
import com.example.thriftstore.utils.SessionManager

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: CustomerViewModel by viewModels {
        val db = ThriftApp.database
        val catRepo = CatalogRepository(db.katalogDao())
        val cartRepo = CartRepository(db.cartDao(), db.orderDao())
        CustomerViewModelFactory(catRepo, cartRepo)
    }

    private var currentProducts: List<Katalog> = emptyList()
    private var currentCartItems: List<CartItemWithProduct> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        val adapter = CartAdapter {} // Read-only adapter (maybe hide delete button)
        binding.rvCheckoutItems.layoutManager = LinearLayoutManager(context)
        binding.rvCheckoutItems.adapter = adapter

        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            currentProducts = products
            calculateTotal()
        }

        if (userId != -1) {
            viewModel.getCartItems(userId).observe(viewLifecycleOwner) { items ->
                currentCartItems = items
                adapter.submitList(items)
                calculateTotal()
            }
        }

        binding.btnPlaceOrder.setOnClickListener {
            if (currentCartItems.isNotEmpty()) {
                val note = binding.etNote.text.toString().trim().takeIf { it.isNotEmpty() }
                val cartItemsOnly = currentCartItems.map { it.cartItem }
                viewModel.checkout(userId, cartItemsOnly, currentProducts, note)
                Toast.makeText(context, "Pesanan Berhasil Dibuat", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_checkoutFragment_to_customerOrderListFragment)
            }
        }
    }

    private fun calculateTotal() {
        var total = 0.0
        currentCartItems.forEach { itemWithProduct ->
            total += itemWithProduct.product.price * itemWithProduct.cartItem.quantity
        }
        binding.tvCheckoutTotal.text = "Rp $total"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
