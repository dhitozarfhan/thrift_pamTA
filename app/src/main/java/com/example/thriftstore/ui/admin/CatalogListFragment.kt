package com.example.thriftstore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.thriftstore.R
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.repository.CatalogRepository
import com.example.thriftstore.databinding.FragmentCatalogListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CatalogListFragment : Fragment() {

    private var _binding: FragmentCatalogListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatalogViewModel by viewModels {
        val repo = CatalogRepository(ThriftApp.database.katalogDao())
        CatalogViewModelFactory(repo)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCatalogListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CatalogAdapter(
            onEditClick = { katalog ->
                val bundle = bundleOf("productId" to katalog.id)
                findNavController().navigate(R.id.action_catalogListFragment_to_addEditCatalogFragment, bundle)
            },
            onDeleteClick = { katalog ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete ${katalog.name}?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.delete(katalog)
                        Toast.makeText(context, "Product deleted", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        )

        binding.rvCatalog.layoutManager = LinearLayoutManager(context)
        binding.rvCatalog.adapter = adapter

        viewModel.allKatalog.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
            binding.tvEmptyCatalog.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAddProduct.setOnClickListener {
             findNavController().navigate(R.id.action_catalogListFragment_to_addEditCatalogFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
