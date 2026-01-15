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
import com.example.thriftstore.databinding.FragmentCartBinding
import com.example.thriftstore.utils.SessionManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
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
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId()

        val adapter = CartAdapter { item ->
            viewModel.removeFromCart(item)
        }

        binding.rvCartItems.layoutManager = LinearLayoutManager(context)
        binding.rvCartItems.adapter = adapter

        // Observe products to calculate prices
        viewModel.allProducts.observe(viewLifecycleOwner) { products ->
            currentProducts = products
            calculateTotal()
        }

        if (userId != -1) {
            viewModel.getCartItems(userId).observe(viewLifecycleOwner) { items ->
                currentCartItems = items
                adapter.submitList(items)
                binding.tvEmptyCart.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
                calculateTotal()
            }
        }

        binding.btnCheckout.setOnClickListener {
            if (currentCartItems.isNotEmpty()) {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
            } else {
                Toast.makeText(context, "Keranjang kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateTotal() {
        var total = 0.0
        currentCartItems.forEach { itemWithProduct ->
            total += itemWithProduct.product.price * itemWithProduct.cartItem.quantity
        }
        binding.tvCartTotal.text = "Rp $total"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
