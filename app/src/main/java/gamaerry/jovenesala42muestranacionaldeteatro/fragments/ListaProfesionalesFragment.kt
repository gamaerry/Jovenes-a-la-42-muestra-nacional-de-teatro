package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.MainActivity
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.ProfesionalesAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentListaProfesionalesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.guardados
import gamaerry.jovenesala42muestranacionaldeteatro.ocultarTeclado
import gamaerry.jovenesala42muestranacionaldeteatro.setGuardado
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListaProfesionalesFragment : Fragment() {
    // los binding enlazan a las vistas con el codigo
    private var _binding: FragmentListaProfesionalesBinding? = null
    private val binding get() = _binding!!

    // gracias a la inyeccion de dependencias no uso constructor sin embargo,
    // puesto que eran necesarios dos adapters hago uso de las etiquetas de hilt
    @Inject
    lateinit var profesionalesAdapter: ProfesionalesAdapter

    // uso un solo viewModel para todas las operaciones de la base de datos
    private val viewModelPrincipal: ViewModelPrincipal by activityViewModels()

    private val seleccionados: ArrayList<MaterialCardView> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).setItemNavegacionInicio()
        if (seleccionados.isEmpty())
            mostrarAcomodo()
        else ocultarAcomodo()

        // creado el fragmento se consiguen todos a los
        // profesionales con palabrasClaves establecidas en ""
        viewModelPrincipal.setListaProfesionales(false)
        binding.miRecyclerView.adapter = profesionalesAdapter

        // de aqui es donde el adapter consigue en
        // tiempo real la listaProfesionalesDeTeatro
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.listaInicio.collect {
                    profesionalesAdapter.submitList(it)
                }
            }
        }

        // se consigue de la propiedad esLineal del viewModel
        // para establecer el acomodo de la lista adecuado
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.inicioEsLineal.collect {
                    binding.miRecyclerView.layoutManager = getLayoutManager(it)
                }
            }
        }

        // cambia el src del icono al ser presionado
        // notese que para getIcono() se cambia el valor
        // de esLineal del viewModel con cada llamada
        binding.acomodo.setOnClickListener { (it as ImageView).setImageDrawable(getIcono()) }


        binding.guardado.setOnClickListener { _ ->
            seleccionados.forEach {
                if (it.isChecked)
                    requireActivity().setGuardado(it.transitionName)
            }
            viewModelPrincipal.updateGuardados(requireActivity().guardados)
        }

        // cuando se presiona el item necesitamos enfocar dicho profesional
        // y realizar la transicion al DetallesProfesionalesFragment
        profesionalesAdapter.accionAlPresionar = { profesionalDelTeatro, itemView ->
            viewModelPrincipal.setProfesionalEnfocado(profesionalDelTeatro)
            getTransicion(itemView).commit()
        }

        profesionalesAdapter.accionAlPresionarLargo = {
            it.isChecked = !it.isChecked
            if (it.isChecked)
                seleccionados.add(it)
            else seleccionados.remove(it)
            if (seleccionados.isEmpty())
                mostrarAcomodo()
            else ocultarAcomodo()
            true
        }


        // OnQueryTextListener es una interfaz que requiere la
        // implementacion de dos métodos, uno para cuando cambia el
        // String de busqueda y otro para cuando se da al boton de buscar
        binding.busqueda.setOnQueryTextListener(buscar(view))
    }

    private fun buscar(view: View): SearchView.OnQueryTextListener {
        return object : SearchView.OnQueryTextListener {
            // esta funcion se llama cuando se presiona el
            // icono de buscar en el SearchView o en el teclado
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    view.ocultarTeclado()
                    viewModelPrincipal.setPalabrasClave(query, false)
                } else false
            }

            // y esta se llama cuando cambia el texto introducido
            // (notese que en ambas funciones setPalabrasClave()
            // regresa un true indicando su correcto funcionamiento)
            override fun onQueryTextChange(query: String?): Boolean {
                return if (query != null)
                    viewModelPrincipal.setPalabrasClave(query, false)
                else false
            }
        }

    }

    private fun mostrarAcomodo(){
        binding.acomodo.visibility = View.VISIBLE
        binding.guardado.visibility = View.GONE
    }

    private fun ocultarAcomodo(){
        binding.acomodo.visibility = View.GONE
        binding.guardado.visibility = View.VISIBLE
    }

    private fun getIcono(): Drawable? {
        // esta funcion confia en que esLineal siempre tiene un valor inicial de
        // true por eso esLineal no esta establecido en los sharedPreferences
        return if (viewModelPrincipal.switchInicioEsLineal())
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid)
        else
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_list)
    }

    private fun getLayoutManager(esLineal: Boolean): LayoutManager {
        // a diferencia de getIcono, aqui no queremos que esLineal
        // cambie con cada llamada pues esta se hara con cada cambio
        // de configuracion y cada aparicion de este Fragment
        return if (esLineal)
            LinearLayoutManager(requireContext())
        else
            GridLayoutManager(requireContext(), 2)
    }

    override fun onResume() {
        super.onResume()
        exitTransition = null
        reenterTransition = null
    }

    private fun getTransicion(itemView: View): FragmentTransaction {
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300L
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
        return requireActivity().supportFragmentManager.beginTransaction()
            // Agrega la transición compartida desde el objeto seleccionado
            .addSharedElement(itemView, "detallesProfesionales")
            // reemplaza (no agrega) el DetallesProfesionalesFragment
            .replace(R.id.contenedorPrincipal, DetallesProfesionalesFragment())
            // se guarda con la etiqueta correspondiente
            .addToBackStack("detallesProfesionales")
    }

    // se sobreescriben estos metodos unicamente para
    // el correcto funcionamiento del nuestro binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaProfesionalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}