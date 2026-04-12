package com.conectasalud.app.ui.turnos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.conectasalud.app.R
import com.conectasalud.app.data.model.Turno
import com.conectasalud.app.databinding.ItemTurnoBinding

class TurnoAdapter(
    private val onCancelClick: (Turno) -> Unit
) : RecyclerView.Adapter<TurnoAdapter.TurnoViewHolder>() {

    private var turnos: List<Turno> = emptyList()

    fun submitList(newTurnos: List<Turno>) {
        turnos = newTurnos
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TurnoViewHolder {
        val binding = ItemTurnoBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TurnoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TurnoViewHolder, position: Int) {
        holder.bind(turnos[position])
    }

    override fun getItemCount() = turnos.size

    inner class TurnoViewHolder(private val binding: ItemTurnoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(turno: Turno) {
            binding.tvEspecialidad.text = turno.especialidad
            binding.tvClinica.text = turno.clinica
            binding.tvFechaHora.text = "${turno.fecha}  •  ${turno.hora}"

            // Estado display & color
            val (estadoText, estadoColor, indicatorColor) = when (turno.estado) {
                "pendiente" -> Triple("Pendiente", R.color.purple_primary, R.color.purple_primary)
                "completado" -> Triple("Completado", R.color.success, R.color.success)
                "cancelado" -> Triple("Cancelado", R.color.error, R.color.error)
                else -> Triple(turno.estado, R.color.text_secondary, R.color.text_secondary)
            }

            binding.tvEstado.text = estadoText
            binding.tvEstado.setTextColor(
                ContextCompat.getColor(binding.root.context, estadoColor)
            )
            binding.viewIndicator.setBackgroundColor(
                ContextCompat.getColor(binding.root.context, indicatorColor)
            )

            // Show cancel button only for pending turnos
            if (turno.estado == "pendiente") {
                binding.btnCancelar.isEnabled = true
                binding.btnCancelar.alpha = 1f
            } else {
                binding.btnCancelar.isEnabled = false
                binding.btnCancelar.alpha = 0.3f
            }

            binding.btnCancelar.setOnClickListener {
                if (turno.estado == "pendiente") {
                    onCancelClick(turno)
                }
            }
        }
    }
}
