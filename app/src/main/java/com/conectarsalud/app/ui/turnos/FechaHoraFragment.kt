package com.conectarsalud.app.ui.turnos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.conectarsalud.app.data.repository.TurnoRepository
import com.conectarsalud.app.data.repository.UserRepository
import com.conectarsalud.app.databinding.FragmentFechaHoraBinding
import com.conectarsalud.app.utils.Constants
import com.conectarsalud.app.utils.hide
import com.conectarsalud.app.utils.show
import com.google.android.material.chip.Chip
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FechaHoraFragment : Fragment() {

    private var _binding: FragmentFechaHoraBinding? = null
    private val binding get() = _binding!!

    private val args: FechaHoraFragmentArgs by navArgs()

    private val viewModel: FechaHoraViewModel by viewModels {
        FechaHoraViewModelFactory(TurnoRepository(), UserRepository(requireContext()))
    }

    private var selectedFecha: String = ""
    private var selectedHora: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFechaHoraBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvEspecialidadNombre.text = args.especialidad
        setupCalendar()
        setupTimeChips()
        setupListeners()
        observeViewModel()
    }

    private fun setupCalendar() {
        // Set minimum date to today
        binding.calendarView.minDate = System.currentTimeMillis() - 1000

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val dayName = SimpleDateFormat("EEEE", Locale("es", "AR")).format(cal.time)
            selectedFecha = "${dayName.replaceFirstChar { it.uppercase() }}, ${sdf.format(cal.time)}"
            updateReservarButton()
        }
    }

    private fun setupTimeChips() {
        binding.chipGroupTime.removeAllViews()
        Constants.HORARIOS_DISPONIBLES.forEach { hora ->
            val chip = Chip(requireContext()).apply {
                text = hora
                isCheckable = true
                chipBackgroundColor = resources.getColorStateList(
                    com.conectarsalud.app.R.color.chip_color_selector,
                    requireContext().theme
                )
                setTextColor(resources.getColorStateList(
                    com.conectarsalud.app.R.color.chip_text_selector,
                    requireContext().theme
                ))
                chipStrokeWidth = 1.5f
                chipStrokeColor = resources.getColorStateList(
                    com.conectarsalud.app.R.color.chip_stroke_selector,
                    requireContext().theme
                )
            }
            binding.chipGroupTime.addView(chip)
        }

        binding.chipGroupTime.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds[0])
                selectedHora = chip?.text.toString()
            } else {
                selectedHora = ""
            }
            updateReservarButton()
        }
    }

    private fun updateReservarButton() {
        binding.btnReservar.isEnabled = selectedFecha.isNotBlank() && selectedHora.isNotBlank()
        binding.btnReservar.alpha = if (binding.btnReservar.isEnabled) 1f else 0.5f
    }

    private fun setupListeners() {
        binding.ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnReservar.setOnClickListener {
            val clinica = Constants.CLINICAS[args.especialidad] ?: "Centro Médico"
            viewModel.reservarTurno(
                especialidad = args.especialidad,
                clinica = clinica,
                fecha = selectedFecha,
                hora = selectedHora
            )
        }
    }

    private fun observeViewModel() {
        viewModel.bookingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is BookingState.Loading -> {
                    binding.progressBar.show()
                    binding.btnReservar.isEnabled = false
                }
                is BookingState.Success -> {
                    binding.progressBar.hide()
                    val clinica = Constants.CLINICAS[args.especialidad] ?: "Centro Médico"
                    val action = FechaHoraFragmentDirections.actionFechaHoraToConfirmacion(
                        especialidad = args.especialidad,
                        clinica = clinica,
                        fecha = selectedFecha,
                        hora = selectedHora
                    )
                    findNavController().navigate(action)
                }
                is BookingState.Error -> {
                    binding.progressBar.hide()
                    binding.btnReservar.isEnabled = true
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_LONG).show()
                }
                else -> {
                    binding.progressBar.hide()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
