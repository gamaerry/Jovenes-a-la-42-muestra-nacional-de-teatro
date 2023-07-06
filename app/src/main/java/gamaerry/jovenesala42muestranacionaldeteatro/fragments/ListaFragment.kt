package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialElevationScale
import gamaerry.jovenesala42muestranacionaldeteatro.MainActivity
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.ProfesionalesAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.ocultarTeclado
import gamaerry.jovenesala42muestranacionaldeteatro.removeGuardado
import gamaerry.jovenesala42muestranacionaldeteatro.addGuardado
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentListaBinding
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import javax.inject.Inject

abstract class ListaFragment : Fragment() {
    // los binding enlazan a las vistas con el codigo
    private var _binding: FragmentListaBinding? = null
    protected val binding get() = _binding!!

    // se van guardando aqui los items seleccionados
    private val seleccionados: ArrayList<MaterialCardView> = ArrayList()

    // gracias a la inyeccion de dependencias no uso constructor
    @Inject
    protected lateinit var profesionalesAdapter: ProfesionalesAdapter

    // uso un solo viewModel para todas las operaciones de la base de datos
    protected val viewModelPrincipal: ViewModelPrincipal by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.miRecyclerView.adapter = profesionalesAdapter

        // cambia el src del icono al ser presionado
        // notese que para getIcono() se cambia el valor
        // de esLineal del viewModel con cada llamada
        binding.acomodo.setOnClickListener { cambiarAcomodo() }

        // se define que va a pasar con el icono
        // que se encarga del guardado de profesionales
        binding.guardado.setOnClickListener { accionDelGuardado() }

        // cuando se presiona el item necesitamos enfocar dicho profesional
        // y realizar la transicion al DetallesProfesionalesFragment
        profesionalesAdapter.accionAlPresionar = { profesionalDelTeatro, itemView ->
            viewModelPrincipal.setProfesionalEnfocado(profesionalDelTeatro)
            getTransicion(itemView).commit()
        }

        // se selecciona al cardView al mantener presionado
        profesionalesAdapter.accionAlPresionarLargo = { seleccionar(it) }

        // OnQueryTextListener es una interfaz que requiere la
        // implementacion de dos métodos, uno para cuando cambia el
        // String de busqueda y otro para cuando se da al boton de buscar
        binding.busqueda.setOnQueryTextListener(buscar())
    }

    fun cerrarBusqueda() {
        if (!binding.busqueda.query.isNullOrEmpty()){
            binding.busqueda.setOnQueryTextListener(null)
            binding.busqueda.onActionViewCollapsed()
            binding.busqueda.setOnQueryTextListener(buscar())
        }
    }

    private fun limpiarSeleccion() {
        // en cada creacion de este fragment se establece el check de cada
        // cardView en false (así como color de seleccionado que se reinicia)
        seleccionados.forEach {
            it.isChecked = false
        }
        seleccionados.clear()
    }

    private fun buscar(): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            // esta funcion se llama cuando se presiona el
            // icono de buscar en el SearchView o en el teclado
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    regresarEstadoPredeterminado()
                    requireView().ocultarTeclado()
                    viewModelPrincipal.buscar(query)
                } else false
            }

            // y esta se llama cuando cambia el texto introducido
            // (notese que en ambas funciones setPalabrasClave()
            // regresa un true indicando su correcto funcionamiento)
            override fun onQueryTextChange(query: String?): Boolean {
                return if (query != null && binding.busqueda.width > 0) {
                    regresarEstadoPredeterminado()
                    // el searchView por su funcion deberia ser uno solo y estar en
                    // el activity sin embargo por cuestiones de diseno se queda aqui
                    (requireActivity() as MainActivity).restablecerExpandableListView()
                    viewModelPrincipal.buscar(query)
                } else false
            }
        }
    }

    private fun mostrarAcomodo() {
        binding.acomodo.visibility = View.VISIBLE
        binding.guardado.visibility = View.GONE
    }

    private fun ocultarAcomodo() {
        binding.acomodo.visibility = View.GONE
        binding.guardado.visibility = View.VISIBLE
    }

    private fun cambiarAcomodo() {
        // esta funcion confia en que esLineal siempre tiene un valor inicial de
        // true por eso esLineal no esta establecido en los sharedPreferences
        if (viewModelPrincipal.enGuardados.value)
            viewModelPrincipal.switchGuardadosEsLineal()
        else
            viewModelPrincipal.switchInicioEsLineal()
    }

    fun getLayoutManager(esLineal: Boolean): RecyclerView.LayoutManager {
        // a diferencia de getIcono, aqui no queremos que esLineal
        // cambie con cada llamada pues esta se hara con cada cambio
        // de configuracion y cada aparicion de este Fragment
        return if (esLineal)
            LinearLayoutManager(requireContext())
        else
            GridLayoutManager(requireContext(), 2)
    }

    private fun getTransicion(itemView: View): FragmentTransaction {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 250L
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 250L
        }
        return requireActivity().supportFragmentManager.beginTransaction()
            // Agrega la transición compartida desde el objeto seleccionado
            .addSharedElement(itemView, "detallesProfesionales")
            // reemplaza (no agrega) el DetallesProfesionalesFragment
            .replace(R.id.contenedorPrincipal, DetallesProfesionalesFragment())
            // se guarda con la etiqueta correspondiente
            .addToBackStack("detallesProfesionales")
    }

    // todo: descubrir porque ListaProfesionalesFragment la ocupa y ListaGuardadosFragment no
    override fun onResume() {
        super.onResume()
        exitTransition = null
        reenterTransition = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun seleccionar(cardView: MaterialCardView): Boolean {
        cardView.isChecked = !cardView.isChecked
        if (cardView.isChecked)
            seleccionados.add(cardView)
        else seleccionados.remove(cardView)
        if (seleccionados.isEmpty()) {
            setComportamientoCabeceraPorDefecto()
            regresarEstadoPredeterminado()
        } else {
            (binding.cabecera.layoutParams as AppBarLayout.LayoutParams).scrollFlags = 0
            (requireActivity() as MainActivity).desaparecerNavegacion()
            ocultarAcomodo()
        }
        return true
    }

    private fun accionDelGuardado() {
        seleccionados.forEach {
            if (it.isChecked)
                if (viewModelPrincipal.enGuardados.value) {
                    requireActivity().removeGuardado(it.transitionName)
                    viewModelPrincipal.removeGuardado(it.transitionName.toInt())
                    Toast.makeText(
                        requireActivity(),
                        "Profesional(es) eliminado(s)",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    requireActivity().addGuardado(it.transitionName)
                    viewModelPrincipal.addGuardado(it.transitionName)
                    Toast.makeText(
                        requireActivity(),
                        "Profesional(es) guardado(s)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        regresarEstadoPredeterminado()
        setComportamientoCabeceraPorDefecto()
    }

    fun regresarEstadoPredeterminado() {
        // es necesario en cada creacion limpiar la
        // seleccion y mostrar el icono de acomodo
        limpiarSeleccion()
        mostrarAcomodo()
        (requireActivity() as MainActivity).aparecerNavegacion()
    }

    private fun setComportamientoCabeceraPorDefecto() {
        (binding.cabecera.layoutParams as AppBarLayout.LayoutParams).scrollFlags =
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                    AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
    }
}