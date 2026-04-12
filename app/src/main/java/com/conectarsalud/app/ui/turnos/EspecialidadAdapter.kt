package com.conectarsalud.app.ui.turnos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.conectarsalud.app.R
import com.conectarsalud.app.databinding.ItemEspecialidadBinding

class EspecialidadAdapter(
    private val especialidades: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<EspecialidadAdapter.EspecialidadViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspecialidadViewHolder {
        val binding = ItemEspecialidadBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return EspecialidadViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EspecialidadViewHolder, position: Int) {
        holder.bind(especialidades[position])
    }

    override fun getItemCount() = especialidades.size

    inner class EspecialidadViewHolder(private val binding: ItemEspecialidadBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(especialidad: String) {
            binding.tvEspecialidad.text = especialidad
            binding.root.setOnClickListener {
                onItemClick(especialidad)
            }
        }
    }
}
