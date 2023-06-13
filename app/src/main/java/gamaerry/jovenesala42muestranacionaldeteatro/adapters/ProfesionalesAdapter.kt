package gamaerry.jovenesala42muestranacionaldeteatro.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.CabeceraBinding
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ItemCompaneroBinding
import javax.inject.Inject
import javax.inject.Singleton

// en el contexto de los adapter en android "item" hace referencia a los contenedores que se reutilizan
// (de ahi el termino recyclerView) para mostrar informacion, en este caso emitida por nuestro viewModel
@Singleton
class ProfesionalesAdapter
@Inject
constructor() : ListAdapter<ProfesionalDelTeatro, RecyclerView.ViewHolder>(ProfesionalDiffUtil) {
    // accion al presionar contenedor del profesional
    lateinit var accionAlPresionarItem: (ProfesionalDelTeatro) -> Unit

    // accion al presionar icono del acomodo 
    lateinit var accionAlPresionarIcono: (ImageView) -> Unit

    // accion al presionar la barra de busqueda (notese
    // que no puede ser un lambda por tener dos funciones)
    lateinit var accionAlPresionarBusqueda: (SearchView) -> SearchView.OnQueryTextListener

    // define los valores de cada tipo de item dada su posicion de aparicion
    // (0 para la cabecera y 1 para los contenedores de cada profesional ie, con esta
    // implementacion establecemos que la cabecera -viewType 0- sera el de la posicion 0
    // y a los demas items -viewType 1- no se le asignará una posición específica)
    override fun getItemViewType(posicionDeAparicion: Int) = if (posicionDeAparicion == 0) 0 else 1

    // dependiendo del tipo de Item se crea el viewHolder correspondiente
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> CabeceraViewHolder(
            CabeceraBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        // notese que seria mas elegante y correcto que el compilador me deje especificar 1 en lugar de
        // else (pues con la funcion anterior me aseguro de que el viewType nunca sera diferente a 0 y 1)
        else -> ProfesionalDelTeatroViewHolder(
            ItemCompaneroBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // una vez creada la instancia correcta del viewHolder se llama a su correspondiente funcion de enlace
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, posicionDeAparicion: Int) =
        if (posicionDeAparicion == 0)
            (viewHolder as CabeceraViewHolder).enlazar()
        else
            (viewHolder as ProfesionalDelTeatroViewHolder).enlazar(getItem(posicionDeAparicion - 1))

    // getItemCount hace referencia a los items contenedores (n)
    // y currentList.size a los elementos de la lista actual (n+1)
    override fun getItemCount() = currentList.size + 1

    inner class CabeceraViewHolder(binding: CabeceraBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // las unicas view de la cabecera del recyclerView
        private val icono = binding.icono
        private val busqueda = binding.barraDeBusqueda

        fun enlazar() {
            // los lambda que se definen en el fragment obtienen desde aqui la referenca a ambos view
            icono.setOnClickListener { accionAlPresionarIcono(it as ImageView) }
            busqueda.setOnQueryTextListener( accionAlPresionarBusqueda(busqueda) )
        }
    }

    inner class ProfesionalDelTeatroViewHolder(binding: ItemCompaneroBinding) :
        RecyclerView.ViewHolder(binding.root) {
        // cada una de las view de los item
        private val imagen = binding.imagen
        private val nombre = binding.nombre
        private val especialidad = binding.especialidad

        // en esta funcion cada item del recyclerView recibe a su respectivo profesional
        fun enlazar(profesional: ProfesionalDelTeatro) {
            itemView.setOnClickListener { accionAlPresionarItem(profesional) }
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