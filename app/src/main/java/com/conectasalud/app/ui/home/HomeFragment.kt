package com.conectasalud.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.conectasalud.app.R
import com.conectasalud.app.data.repository.TurnoRepository
import com.conectasalud.app.data.repository.UserRepository
import com.conectasalud.app.databinding.FragmentHomeBinding
import com.conectasalud.app.utils.hide
import com.conectasalud.app.utils.show

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
            // Navigate via bottom navigation
            requireActivity().findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottom_navigation
            )?.selectedItemId = R.id.misTurnosFragment
        }
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
