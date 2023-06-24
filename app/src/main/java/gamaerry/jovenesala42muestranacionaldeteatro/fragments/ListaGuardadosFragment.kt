package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.ProfesionalesAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentListaProfesionalesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.guardados
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ListaGuardadosFragment : ListaFragment() {
    @Inject
    override lateinit var profesionalesAdapter: ProfesionalesAdapter
    override val viewModelPrincipal: ViewModelPrincipal by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListaProfesionalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // es necesario en cada creacion limpiar la
        // seleccion y mostrar el icono de acomodo
        limpiarSeleccion()
        mostrarAcomodo()

        // creado el fragmento se consiguen
        // todos a los profesionales guardados
        requireActivity().guardados?.forEach {
            viewModelPrincipal.addGuardado(it)
        }
        binding.miRecyclerView.adapter = profesionalesAdapter

        // de aqui es donde el otro adapter
        // consigue en tiempo real la listaGuardada
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.listaGuardados.collect {
                    profesionalesAdapter.submitList(it)
                }
            }
        }

        // se consigue de la propiedad esLineal del viewModel
        // para establecer el acomodo de la lista adecuado
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.guardadosEsLineal.collect {
                    binding.miRecyclerView.layoutManager = getLayoutManager(it)
                }
            }
        }

        // cambia el src del icono al ser presionado
        // notese que para getIcono() se cambia el valor
        // de esLineal del viewModel con cada llamada
        binding.acomodo.setOnClickListener { (it as ImageView).setImageDrawable(getIcono()) }

        binding.guardado.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_unarchive))
        binding.guardado.setOnClickListener { accionDelGuardado(true) }

        // cuando se presiona el item necesitamos enfocar dicho profesional
        // y realizar la transicion al DetallesProfesionalesFragment
        profesionalesAdapter.accionAlPresionar = { profesionalDelTeatro, itemView ->
            viewModelPrincipal.setProfesionalEnfocado(profesionalDelTeatro)
            getTransicion(itemView).commit()
        }

        profesionalesAdapter.accionAlPresionarLargo = { seleccionar(it) }

        // OnQueryTextListener es una interfaz que requiere la
        // implementacion de dos métodos, uno para cuando cambia el
        // String de busqueda y otro para cuando se da al boton de buscar
        binding.busqueda.setOnQueryTextListener(buscar(view, true))
    }
}