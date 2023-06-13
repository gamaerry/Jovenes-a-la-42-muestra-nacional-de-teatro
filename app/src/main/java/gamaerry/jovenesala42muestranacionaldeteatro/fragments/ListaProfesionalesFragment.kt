package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.ProfesionalesAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentListaProfesionalesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.ocultarTeclado
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListaProfesionalesFragment : Fragment() {
    private var _binding: FragmentListaProfesionalesBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var profesionalesAdapter: ProfesionalesAdapter
    private val viewModelPrincipal: ViewModelPrincipal by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelPrincipal.getProfesionales()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.listaProfesionalesDeTeatro.collect {
                    profesionalesAdapter.submitList(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.esLineal.collect {
                    binding.miRecyclerView.layoutManager = getLayoutManager(it)
                }
            }
        }

        binding.miRecyclerView.adapter = profesionalesAdapter

        profesionalesAdapter.accionAlPresionarBusqueda = accionAlBuscar()

        profesionalesAdapter.accionAlPresionarIcono = { it.setImageDrawable(getIcono()) }

        profesionalesAdapter.accionAlPresionarItem = {
            viewModelPrincipal.setProfesionalEnfocado(it)
            getTransicion().commit()
        }
    }

    private fun accionAlBuscar(): (SearchView) -> SearchView.OnQueryTextListener = {
        object : SearchView.OnQueryTextListener {
            // esta funcion se llama cuando se presiona el
            // icono de buscar en el SearchView o en el teclado
            override fun onQueryTextSubmit(query: String?): Boolean {
                return if (query != null) {
                    it.ocultarTeclado()
                    viewModelPrincipal.setPalabrasClave(query)
                } else false
            }

            // y esta se llama cuando cambia el texto introducido
            // (notese que en ambas funciones setBusquedaQuery()
            // regresa un true indicando su correcto funcionamiento)
            override fun onQueryTextChange(query: String?): Boolean {
                return if (query != null)
                    viewModelPrincipal.setPalabrasClave(query)
                else false
            }
        }
    }

    private fun getIcono(): Drawable? {
        return if (viewModelPrincipal.switchEsLineal())
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid)
        else
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_list)
    }

    private fun getLayoutManager(esLineal: Boolean): LayoutManager {
        return if (esLineal)
            LinearLayoutManager(requireContext())
        else
            GridLayoutManager(requireContext(), 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int =
                        if (position == 0) 2 // Tamaño completo para la cabecera en la posición 0
                        else 1 // Tamaño normal para los elementos regulares
                }
            }
    }

    private fun getTransicion(): FragmentTransaction {
        return requireActivity().supportFragmentManager.beginTransaction().apply {
            setCustomAnimations(
                R.anim.entrar_desde_derecha,
                R.anim.salir_hacia_izquierda,
                R.anim.entrar_desde_izquierda,
                R.anim.salir_hacia_derecha
            )
            replace(R.id.contenedorPrincipal, DetallesProfesionalesFragment())
            addToBackStack("detallesProfesionales")
        }
    }

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