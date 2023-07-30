package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.card.MaterialCardView
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemCompaneroBinding
import javax.inject.Inject

// en el contexto de los adapter en android "item" hace referencia a los contenedores que se reutilizan
// (de ahi el termino recyclerView) para mostrar informacion, en este caso emitida por nuestro viewModel
class ProfesionalesAdapter
@Inject
constructor() :
    ListAdapter<ProfesionalDelTeatro, ProfesionalesAdapter.ProfesionalDelTeatroViewHolder>(
        ProfesionalDiffUtil
    ) {
    init {
        // con esta linea evitamos que nuestro recyclerView se regrese al principio cuando se restaure
        // (para esto fue necesario implementar la dependencia especifica de RecyclerView en el build.gradle)
        stateRestorationPolicy = StateRestorationPolicy.PREVENT_WHEN_EMPTY
    }

    // accion al presionar contenedor del profesional
    lateinit var accionAlPresionar: (ProfesionalDelTeatro, View) -> Unit

    // accion al presionar contenedor del profesional
    lateinit var accionAlPresionarLargo: (MaterialCardView) -> Boolean

    // se crea el viewHolder correspondiente
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ProfesionalDelTeatroViewHolder(
            ItemCompaneroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    // una vez creada la instancia del viewHolder se llama a su correspondiente funcion de enlace
    override fun onBindViewHolder(holder: ProfesionalDelTeatroViewHolder, i: Int) =
        holder.enlazar(getItem(i))

    // getItemCount hace referencia a los elementos del adapter (items y objetos Profesionales)
    override fun getItemCount() = currentList.size

    inner class ProfesionalDelTeatroViewHolder(binding: ItemCompaneroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // cada una de las view de los item
        private val imagen = binding.imagen
        private val nombre = binding.nombre
        private val especialidad = binding.especialidad

        // en esta funcion cada item del recyclerView recibe a su respectivo profesional
        fun enlazar(profesional: ProfesionalDelTeatro) {
            // se establece el transitionName de cada item con el id de su
            // respectivo profesional para poder guardar o eliminar a este
            // (mediante su id) en el conjunto mutable guardados
            itemView.transitionName = profesional.id.toString()
            // puesto que el viewModel esta solo al alcance de los fragment,
            // la acción se establece en el ListaFragment y esta linea solo
            // proporciona al profesional y al view necesarios
            itemView.setOnClickListener { accionAlPresionar(profesional, it) }
            // se pasa el view especificamente como un MaterialCardView
            // pues se necesita utilizar su propiedad isChecked
            itemView.setOnLongClickListener { accionAlPresionarLargo(it as MaterialCardView) }
            // las siguientes tres lineas llenan con la informacion
            // del profesional a cada elemento visual del item
            nombre.text = profesional.nombre
            especialidad.text = profesional.especialidades
            imagen.load(profesional.urlImagen)
        }
    }

    // DiffUtil puede evitar explorar más a fondo los elementos de cada objeto para sus operaciones internas
    object ProfesionalDiffUtil : DiffUtil.ItemCallback<ProfesionalDelTeatro>() {

        //identifica un objeto de otro con un solo campo en este caso
        override fun areItemsTheSame(old: ProfesionalDelTeatro, new: ProfesionalDelTeatro) =
            old.id == new.id

        // permite especificar los campos que requieren actualizaciones reales.
        // ie, especificamos cuales campos de nuestro modelo pueden llegar
        // a cambiar con el tiempo para así optimizar las operaciones internas
        // (en este caso todas las variables a excepcion del id que nunca cambiara)
        override fun areContentsTheSame(old: ProfesionalDelTeatro, new: ProfesionalDelTeatro) =
            old.nombre == new.nombre && old.especialidades == new.especialidades &&
                    old.descripcion == new.descripcion && old.estado == new.estado &&
                    old.urlImagen == new.urlImagen
    }
}