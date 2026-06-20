package com.conectarsalud.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.conectarsalud.app.R
import com.conectarsalud.app.data.repository.TurnoRepository
import com.conectarsalud.app.data.repository.UserRepository
import com.conectarsalud.app.databinding.FragmentHomeBinding
import com.conectarsalud.app.utils.hide
import com.conectarsalud.app.utils.show

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            UserRepository(requireContext()),
            TurnoRepository()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshData()
    }

    private fun setupListeners() {
        binding.btnReservar.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_especialidad)
        }

        binding.llMisTurnos.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottom_navigation
            )?.selectedItemId = R.id.misTurnosFragment
        }

        // "Ver detalles" del próximo turno
        binding.tvVerDetalles.setOnClickListener {
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottom_navigation
            )?.selectedItemId = R.id.misTurnosFragment
        }

        // Botón de cerrar sesión
        binding.ivLogout.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Cerrar sesión")
            .setMessage("¿Estás seguro que deseas cerrar sesión?")
            .setPositiveButton("Sí, salir") { _, _ ->
                viewModel.signOut()
                findNavController().navigate(R.id.action_home_logout)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.userName.observe(viewLifecycleOwner) { name ->
            binding.tvGreeting.text = getString(R.string.greeting, name)
        }

        viewModel.proximoTurno.observe(viewLifecycleOwner) { turno ->
            if (turno != null) {
                binding.tvEspecialidadTurno.text = turno.especialidad
                binding.tvFechaTurno.text = "${turno.fecha} - ${turno.hora}"
                binding.tvNoTurno.hide()
            } else {
                binding.tvEspecialidadTurno.text = getString(R.string.no_upcoming)
                binding.tvFechaTurno.text = ""
                binding.tvNoTurno.show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
