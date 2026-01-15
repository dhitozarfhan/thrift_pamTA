package com.example.thriftstore.ui.customer
import coil.load

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.thriftstore.R
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.model.Katalog
import com.example.thriftstore.data.repository.CartRepository
import com.example.thriftstore.data.repository.CatalogRepository
import com.example.thriftstore.databinding.FragmentProductDetailBinding
import com.example.thriftstore.utils.SessionManager
import kotlinx.coroutines.launch

class ProductDetailFragment : Fragment() {

    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager
    private var currentProduct: Katalog? = null

    private val viewModel: CustomerViewModel by viewModels {
        val db = ThriftApp.database
        val catRepo = CatalogRepository(db.katalogDao())
        val cartRepo = CartRepository(db.cartDao(), db.orderDao())
        CustomerViewModelFactory(catRepo, cartRepo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        val productId = arguments?.getInt("productId", -1) ?: -1
        if (productId != -1) {
            loadProduct(productId)
        }

        binding.btnAddToCart.setOnClickListener {
            val qtyStr = binding.etQty.text.toString()
            val qty = qtyStr.toIntOrNull() ?: 1
            if (currentProduct != null) {
                // if (qty > currentProduct!!.stock) { ... } // Stock not in SRS
                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    viewModel.addToCart(userId, currentProduct!!, qty)
                    Toast.makeText(context, "Berhasil ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_productDetailFragment_to_cartFragment)
                } else {
                     Toast.makeText(context, "Silakan login terlebih dahulu", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadProduct(id: Int) {
        lifecycleScope.launch {
            val product = viewModel.getProduct(id)
            if (product != null) {
                currentProduct = product
                binding.tvDetailName.text = product.name
                binding.tvDetailPrice.text = "Rp ${product.price}"
                binding.tvDetailDesc.text = product.description
                
                binding.imgProductDetail.load(product.image_url) {
                    crossfade(true)
                    placeholder(android.R.color.darker_gray)
                    error(android.R.color.darker_gray)
                }
                // Image and is_active could be displayed too
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
