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
import com.example.thriftstore.data.model.User
import com.example.thriftstore.data.repository.AuthRepository
import com.example.thriftstore.databinding.FragmentRegisterBinding
import com.example.thriftstore.utils.SessionManager

class RegisterFragment : Fragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AuthViewModel by viewModels {
        val db = ThriftApp.database
        val repo = AuthRepository(db.userDao(), db.adminDao())
        val sm = SessionManager(requireContext())
        AuthViewModelFactory(repo, sm)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener {
            val name = binding.etNameReg.text.toString().trim()
            val email = binding.etEmailReg.text.toString().trim()
            val phone = binding.etPhoneReg.text.toString().trim()
            val password = binding.etPassReg.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Mohon isi semua bidang", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(context, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Basic email validation (SRS 3.1)
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(context, "Format email tidak valid", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val newUser = User(name = name, email = email, phone = phone, password = password)
            viewModel.register(newUser)
        }

        binding.tvLoginLink.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.registerResult.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(context, "Registrasi Berhasil", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Registrasi Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
