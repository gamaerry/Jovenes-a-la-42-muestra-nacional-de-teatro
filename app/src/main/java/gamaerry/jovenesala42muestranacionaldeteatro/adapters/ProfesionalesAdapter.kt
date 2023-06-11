package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.CabeceraBinding
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemCompaneroBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfesionalesAdapter
@Inject
constructor() : ListAdapter<ProfesionalDelTeatro, RecyclerView.ViewHolder>(ProfesionalDiffUtil) {
    lateinit var accionAlPresionarItem: (ProfesionalDelTeatro) -> Unit
    lateinit var accionAlPresionarIcono: (ImageView) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> CabeceraViewHolder(
            CabeceraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        else -> ProfesionalDelTeatroViewHolder(
            ItemCompaneroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) = when (holder) {
        is ProfesionalDelTeatroViewHolder -> holder.enlazar(getItem(i - 1))
        is CabeceraViewHolder -> holder.enlazar()
        else -> Unit
    }

    override fun getItemCount() = currentList.size + 1

    override fun getItemViewType(position: Int) = if (position == 0) 0 else 1

    inner class CabeceraViewHolder(binding: CabeceraBinding) : RecyclerView.ViewHolder(binding.root){
        private val icono = binding.icono
        fun enlazar(){
            icono.setOnClickListener { accionAlPresionarIcono(it as ImageView) }
        }
    }

    inner class ProfesionalDelTeatroViewHolder(binding: ItemCompaneroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imagen = binding.imagen
        private val nombre = binding.nombre
        private val especialidad = binding.especialidad

        fun enlazar(profesional: ProfesionalDelTeatro) {
            itemView.setOnClickListener { accionAlPresionarItem(profesional) }
            nombre.text = profesional.nombre
            especialidad.text = profesional.especialidades
            imagen.load(profesional.urlImagen)
        }
    }

    object ProfesionalDiffUtil : DiffUtil.ItemCallback<ProfesionalDelTeatro>() {
        override fun areItemsTheSame(old: ProfesionalDelTeatro, new: ProfesionalDelTeatro) =
            old.id == new.id

        override fun areContentsTheSame(old: ProfesionalDelTeatro, new: ProfesionalDelTeatro) =
            old == new
    }
}