package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gamaerry.jovenesala42muestranacionaldeteatro.data.Especialidad
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemEspecialidadBinding

class EspecialidadesAdapter : ListAdapter<Especialidad, EspecialidadesAdapter.EspecialidadViewHolder>(
    EspecialidadDiffUtil
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EspecialidadViewHolder {
        return EspecialidadViewHolder(
            ItemEspecialidadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EspecialidadViewHolder, i: Int) {
        val especialidad = getItem(i)
        holder.enlazar(especialidad)
    }

    class EspecialidadViewHolder(binding: ItemEspecialidadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val botonEspecialidad = binding.especialidad
        fun enlazar(especialidad: Especialidad) {
            botonEspecialidad.text = " ${especialidad.capitalize()} "
        }
    }
    object EspecialidadDiffUtil: DiffUtil.ItemCallback<Especialidad>(){
        override fun areItemsTheSame(oldItem: Especialidad, newItem: Especialidad): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Especialidad, newItem: Especialidad): Boolean {
            return oldItem.toString() == newItem.toString()
        }

    }
}