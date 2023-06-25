package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.guardados
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListaGuardadosFragment : ListaFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regresarEstadoPredeterminado()
        viewModelPrincipal.setEnGuardados(true)

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
                viewModelPrincipal.listaGuardados.collect { listaGuardados ->
                    profesionalesAdapter.submitList(listaGuardados.sortedBy { it.id })
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

        // se define que va a pasar con el icono
        // que se encarga del guardado de profesionales
        binding.guardado.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_unarchive
            )
        )
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
        binding.busqueda.setOnQueryTextListener(buscar(view))
    }
}