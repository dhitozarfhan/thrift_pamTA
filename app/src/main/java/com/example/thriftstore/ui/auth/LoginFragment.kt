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
import com.example.thriftstore.databinding.FragmentLoginBinding
import com.example.thriftstore.utils.SessionManager

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    // Assuming we manually create factory or use a DI in real app. manually here.
    private val viewModel: AuthViewModel by viewModels {
        val db = ThriftApp.database
        val repo = AuthRepository(db.userDao(), db.adminDao())
        sessionManager = SessionManager(requireContext())
        AuthViewModelFactory(repo, sessionManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        // Auto login check
        if (sessionManager.isLoggedIn()) {
            val role = sessionManager.getRole()
            if (role == "admin") {
                findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)
            } else {
                findNavController().navigate(R.id.action_loginFragment_to_customerHomeFragment)
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Mohon isi semua bidang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password, false)
        }

        binding.tvAdminLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_adminLoginFragment)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginResult.SuccessUser -> {
                    sessionManager.createLoginSession(result.user.id, "customer", result.user.name)
                    findNavController().navigate(R.id.action_loginFragment_to_customerHomeFragment)
                }
                is LoginResult.SuccessAdmin -> {
                    sessionManager.createLoginSession(result.admin.id, "admin", result.admin.name)
                    findNavController().navigate(R.id.action_loginFragment_to_adminDashboardFragment)
                }
                is LoginResult.Error -> {
                    Toast.makeText(context, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
