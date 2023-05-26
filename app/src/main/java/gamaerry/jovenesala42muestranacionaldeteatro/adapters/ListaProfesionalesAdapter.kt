package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import gamaerry.jovenesala42muestranacionaldeteatro.data.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.CabeceraBinding
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemCompaneroBinding

class ListaProfesionalesAdapter(private val onClick: (ProfesionalDelTeatro) -> Unit) :
    ListAdapter<ProfesionalDelTeatro, RecyclerView.ViewHolder>(
        ProfesionalDiffUtil
    ) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> CabeceraViewHolder(
                CabeceraBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else ->
                ProfesionalDelTeatroViewHolder(
                    ItemCompaneroBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        when (holder) {
            is CabeceraViewHolder -> holder.enlazar()
            is ProfesionalDelTeatroViewHolder -> {
                val profesional = getItem(i - 1)
                holder.enlazar(profesional)
            }
        }
    }

    override fun getItemCount(): Int {
        // toma en cuenta el encabezado
        return currentList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0 else 1
    }

    inner class CabeceraViewHolder(binding: CabeceraBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val barraDeBusqueda = binding.barraDeBusqueda
        private val iconoLista = binding.iconoLista
        fun enlazar() {
            // Aquí puedes actualizar las vistas del encabezado según sea necesario
        }
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
        override fun areItemsTheSame(
            oldItem: ProfesionalDelTeatro,
            newItem: ProfesionalDelTeatro
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProfesionalDelTeatro,
            newItem: ProfesionalDelTeatro
        ): Boolean {
            return oldItem == newItem
        }

    }
}