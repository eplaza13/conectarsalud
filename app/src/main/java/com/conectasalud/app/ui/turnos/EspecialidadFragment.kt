package com.conectasalud.app.ui.turnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.conectasalud.app.databinding.FragmentEspecialidadBinding
import com.conectasalud.app.utils.Constants

class EspecialidadFragment : Fragment() {

    private var _binding: FragmentEspecialidadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEspecialidadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        val adapter = EspecialidadAdapter(Constants.ESPECIALIDADES) { especialidad ->
            // Navigate to FechaHora with selected specialty
            val action = EspecialidadFragmentDirections
                .actionEspecialidadToFechaHora(especialidad)
            findNavController().navigate(action)
        }

        binding.rvEspecialidades.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
