package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemEspecialidadBinding
import javax.inject.Inject
import javax.inject.Singleton

// No es necesario extender de la clase ListAdapter como
// en el caso del ProfesionalesAdapter pues la lista de
// las especialidades de cada profesional es estatica
@Singleton
class EspecialidadesAdapter
@Inject
constructor() : RecyclerView.Adapter<EspecialidadesAdapter.EspecialidadesViewHolder>() {
    // el adapter recibe desde el fragment la accion
    // (que terminará usando el viewHolder) y el viewHolder
    // mandara el objeto String a procesar al mismo fragment
    // (uno manda la receta y el otro los limones).
    // Normalmente es el viewHolder quien recibe la info
    // por eso es poco intuitivo como funciona esta lambda
    lateinit var accionAlHacerClic: (String) -> Unit

    // tanto este campo como los siguientes metodos
    // son requeridos para la correcta obtencion de
    // los datos que salen desde el fragment y que
    // requiere el viewModel para llenar cada view
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

    // el adapter es finalmente un intermediario entre
    // la informacion del fragment y el uso de esa
    // informacion por cada vista dentro del viewHolder
    inner class EspecialidadesViewHolder(binding: ItemEspecialidadBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // este objeto referencia a cada boton creado
        // a partir del reciclerView de botones
        private val botonEspecialidad = binding.especialidad
        fun enlazar(especialidad: String) {
            // cuando se enlaza la informacion con
            // la vista simplemente se muestra
            botonEspecialidad.text = " $especialidad "
        }
    }
}