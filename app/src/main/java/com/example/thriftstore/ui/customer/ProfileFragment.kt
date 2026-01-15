package com.example.thriftstore.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.thriftstore.R
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.databinding.FragmentProfileBinding
import com.example.thriftstore.utils.SessionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())
        
        val name = sessionManager.getName()
        binding.tvProfileName.text = name

        // Load current address if stored (need to fetch User object)
        // For MVP, just allow saving.
        
        binding.btnSaveProfile.setOnClickListener {
            val address = binding.etAddress.text.toString()
            val userId = sessionManager.getUserId()
            
            if (userId != -1) {
                lifecycleScope.launch {
                    val userDao = ThriftApp.database.userDao()
                    val user = userDao.getUserById(userId)
                    if (user != null) {
                        val newUser = user.copy(address = address)
                        userDao.updateUser(newUser)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Alamat Berhasil Diperbarui", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.btnLogoutUser.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    sessionManager.logout()
                    findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
