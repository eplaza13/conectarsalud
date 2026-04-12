package com.conectarsalud.app.ui.turnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.conectarsalud.app.databinding.FragmentConfirmacionBinding

class ConfirmacionFragment : Fragment() {

    private var _binding: FragmentConfirmacionBinding? = null
    private val binding get() = _binding!!

    private val args: ConfirmacionFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmacionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate confirmation details
        binding.tvEspecialidad.text = args.especialidad
        binding.tvClinica.text = args.clinica
        binding.tvFecha.text = args.fecha
        binding.tvHora.text = args.hora

        binding.btnVolver.setOnClickListener {
            findNavController().navigate(
                ConfirmacionFragmentDirections.actionConfirmacionToHome()
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
