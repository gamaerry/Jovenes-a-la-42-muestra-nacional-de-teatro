package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemEspecialidadBinding
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EspecialidadesAdapter
@Inject
constructor() : RecyclerView.Adapter<EspecialidadesAdapter.EspecialidadesViewHolder>() {
    lateinit var accionAlHacerClic: (String) -> Unit
    lateinit var listaDeEspecialidades: List<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EspecialidadesViewHolder(
            ItemEspecialidadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = listaDeEspecialidades.size

    override fun onBindViewHolder(holder: EspecialidadesViewHolder, i: Int) =
        holder.enlazar(listaDeEspecialidades[i])

    inner class EspecialidadesViewHolder(binding: ItemEspecialidadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val botonEspecialidad = binding.especialidad
        fun enlazar(especialidad: String) {
            botonEspecialidad.text = " $especialidad "
        }
    }
}