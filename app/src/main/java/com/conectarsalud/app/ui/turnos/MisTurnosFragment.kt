package com.conectarsalud.app.ui.turnos

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.conectarsalud.app.data.repository.TurnoRepository
import com.conectarsalud.app.data.repository.UserRepository
import com.conectarsalud.app.databinding.FragmentMisTurnosBinding
import com.conectarsalud.app.utils.hide
import com.conectarsalud.app.utils.show

class MisTurnosFragment : Fragment() {

    private var _binding: FragmentMisTurnosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MisTurnosViewModel by viewModels {
        MisTurnosViewModelFactory(TurnoRepository(), UserRepository(requireContext()))
    }

    private lateinit var turnoAdapter: TurnoAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMisTurnosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        turnoAdapter = TurnoAdapter { turno ->
            // Show cancel confirmation dialog
            AlertDialog.Builder(requireContext())
                .setTitle("Cancelar turno")
                .setMessage("¿Estás seguro que deseas cancelar este turno de ${turno.especialidad}?")
                .setPositiveButton("Sí, cancelar") { _, _ ->
                    viewModel.cancelarTurno(turno.id)
                }
                .setNegativeButton("No, volver", null)
                .show()
        }

        binding.rvTurnos.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = turnoAdapter
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.progressBar.show()
                binding.rvTurnos.hide()
                binding.layoutEmpty.hide()
            } else {
                binding.progressBar.hide()
            }
        }

        viewModel.turnos.observe(viewLifecycleOwner) { turnos ->
            turnoAdapter.submitList(turnos)
            if (turnos.isEmpty()) {
                binding.rvTurnos.hide()
                binding.layoutEmpty.show()
            } else {
                binding.rvTurnos.show()
                binding.layoutEmpty.hide()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
