package com.example.thriftstore.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.thriftstore.R
import com.example.thriftstore.databinding.FragmentDashboardBinding
import com.example.thriftstore.utils.SessionManager

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionManager = SessionManager(requireContext())

        binding.btnLogoutAdmin.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Logout Admin")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    sessionManager.logout()
                    findNavController().navigate(R.id.action_adminDashboardFragment_to_loginFragment)
                }
                .setNegativeButton("Tidak", null)
                .show()
        }

        binding.cardCatalog.setOnClickListener {
             findNavController().navigate(R.id.action_adminDashboardFragment_to_catalogListFragment)
        }

        binding.cardOrders.setOnClickListener {
             findNavController().navigate(R.id.action_adminDashboardFragment_to_orderListFragment)
        }

        binding.cardReport.setOnClickListener {
             findNavController().navigate(R.id.action_adminDashboardFragment_to_salesReportFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
