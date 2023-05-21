package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import gamaerry.jovenesala42muestranacionaldeteatro.data.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemCompaneroBinding

class ListaProfesionalesAdapter(private val onClick: (ProfesionalDelTeatro) -> Unit) :
    ListAdapter<ProfesionalDelTeatro, ListaProfesionalesAdapter.ProfesionalDelTeatroViewHolder>(
        ProfesionalDiffUtil
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfesionalDelTeatroViewHolder {
        return ProfesionalDelTeatroViewHolder(
            ItemCompaneroBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ProfesionalDelTeatroViewHolder, i: Int) {
        val profesional = getItem(i)
        holder.enlazar(profesional)
    }

    inner class ProfesionalDelTeatroViewHolder(binding: ItemCompaneroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imagen = binding.imagen
        private val nombre = binding.nombre
        private val especialidad = binding.especialidad
        private var profesionalActual: ProfesionalDelTeatro? = null

        init {
            itemView.setOnClickListener {
                profesionalActual?.let { onClick(it) }
            }
        }

        fun enlazar(profesional: ProfesionalDelTeatro) {
            profesionalActual = profesional
            nombre.text = profesional.nombre
            especialidad.text = profesional.getStringDeEspecialidades()
            imagen.load(profesional.urlImagen)
        }
    }

    object ProfesionalDiffUtil : DiffUtil.ItemCallback<ProfesionalDelTeatro>() {
        override fun areItemsTheSame(oldItem: ProfesionalDelTeatro, newItem: ProfesionalDelTeatro): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfesionalDelTeatro, newItem: ProfesionalDelTeatro): Boolean {
            return oldItem == newItem
        }

    }
}