package com.example.thriftstore.ui.admin
import coil.load
import android.text.Editable
import android.text.TextWatcher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.model.Katalog
import com.example.thriftstore.data.repository.CatalogRepository
import com.example.thriftstore.databinding.FragmentAddEditCatalogBinding
import kotlinx.coroutines.launch

class AddEditCatalogFragment : Fragment() {

    private var _binding: FragmentAddEditCatalogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels {
        val repo = CatalogRepository(ThriftApp.database.katalogDao())
        CatalogViewModelFactory(repo)
    }

    private var currentId: Int = -1 // -1 means Add Mode

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentId = arguments?.getInt("productId", -1) ?: -1

        if (currentId != -1) {
            binding.tvTitleAddEdit.text = "Edit Produk"
            binding.btnSaveProduct.text = "Perbarui Produk"
            loadProductData(currentId)
        }

        binding.etProdImage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.imgPreviewAdd.load(s.toString()) {
                    crossfade(true)
                    placeholder(android.R.color.darker_gray)
                    error(android.R.color.darker_gray)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        binding.btnSaveProduct.setOnClickListener {
            saveProduct()
        }
    }

    private fun loadProductData(id: Int) {
        lifecycleScope.launch {
            val repo = CatalogRepository(ThriftApp.database.katalogDao())
            val product = repo.getKatalogById(id)
            if (product != null) {
                binding.etProdName.setText(product.name)
                binding.etProdPrice.setText(product.price.toString())
                binding.cbIsActive.isChecked = product.is_active
                binding.etProdDesc.setText(product.description)
                binding.etProdImage.setText(product.image_url)
                
                binding.imgPreviewAdd.load(product.image_url) {
                    crossfade(true)
                    placeholder(android.R.color.darker_gray)
                    error(android.R.color.darker_gray)
                }
            }
        }
    }

    private fun saveProduct() {
        val name = binding.etProdName.text.toString().trim()
        val priceStr = binding.etProdPrice.text.toString().trim()
        // val stockStr = binding.etProdStock.text.toString().trim() // Stock not in SRS
        val desc = binding.etProdDesc.text.toString().trim()

        if (name.isEmpty() || priceStr.isEmpty()) {
             Toast.makeText(context, "Mohon lengkapi semua bidang", Toast.LENGTH_SHORT).show()
             return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        val isActive = binding.cbIsActive.isChecked
        val imageUrl = binding.etProdImage.text.toString().trim()

        val newProduct = Katalog(
            id = if (currentId == -1) 0 else currentId,
            name = name,
            price = price,
            description = desc,
            image_url = imageUrl,
            is_active = isActive
        )

        if (currentId == -1) {
            viewModel.insert(newProduct)
            Toast.makeText(context, "Produk Ditambahkan", Toast.LENGTH_SHORT).show()
        } else {
            viewModel.update(newProduct)
            Toast.makeText(context, "Produk Diperbarui", Toast.LENGTH_SHORT).show()
        }
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
