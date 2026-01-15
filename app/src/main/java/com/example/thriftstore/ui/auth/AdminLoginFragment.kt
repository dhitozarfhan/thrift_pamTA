package com.example.thriftstore.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.thriftstore.R
import com.example.thriftstore.ThriftApp
import com.example.thriftstore.data.repository.AuthRepository
import com.example.thriftstore.databinding.FragmentAdminLoginBinding
import com.example.thriftstore.utils.SessionManager

class AdminLoginFragment : Fragment() {

    private var _binding: FragmentAdminLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    private val viewModel: AuthViewModel by viewModels {
        val db = ThriftApp.database
        val repo = AuthRepository(db.userDao(), db.adminDao())
        sessionManager = SessionManager(requireContext())
        AuthViewModelFactory(repo, sessionManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        binding.btnAdminLogin.setOnClickListener {
            val email = binding.etAdminEmail.text.toString().trim()
            val password = binding.etAdminPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Mohon isi semua bidang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password, true)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginResult.SuccessAdmin -> {
                    sessionManager.createLoginSession(result.admin.id, "admin", result.admin.name)
                    findNavController().navigate(R.id.action_adminLoginFragment_to_adminDashboardFragment)
                }
                is LoginResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
