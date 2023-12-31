package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.MainActivity
import gamaerry.jovenesala42muestranacionaldeteatro.R
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListaInicioFragment : ListaFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regresarEstadoPredeterminado()
        viewModelPrincipal.setEnGuardados(false)

        // esta linea se ocupa cuando de Guardados se hace un popStack a Inicio
        // que por defecto no cambia el item de navegacion correspondiente
        (requireActivity() as MainActivity).setItemNavegacionInicio()

        // de aqui es donde el adapter consigue en
        // tiempo real la listaProfesionalesDeTeatro
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.listaInicio.collect { listaInicio ->
                    if (!viewModelPrincipal.enGuardados.value)
                        (requireActivity() as MainActivity).setContador("${listaInicio.size} resultados")
                    if (viewModelPrincipal.ordenadosPorNombre)
                        profesionalesAdapter.submitList(listaInicio.sortedBy { it.nombre })
                    else
                        profesionalesAdapter.submitList(listaInicio.sortedBy { it.id })
                    binding.textoDeFondo.text = if (listaInicio.isEmpty()) {
                        binding.textoDeFondo.visibility = View.VISIBLE
                        binding.miRecyclerView.visibility = View.GONE
                        "No hay profesionales que cumplan con los filtros de búsqueda."
                    } else {
                        binding.textoDeFondo.visibility = View.GONE
                        binding.miRecyclerView.visibility = View.VISIBLE
                        ""
                    }
                }
            }
        }

        // se consigue de la propiedad esLineal del viewModel
        // para establecer el acomodo de la lista adecuado
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.inicioEsLineal.collect {
                    binding.miRecyclerView.layoutManager = getLayoutManager(it)
                    binding.acomodo.setImageDrawable(
                        if (it)
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid)
                        else
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_list)
                    )
                }
            }
        }
    }
}