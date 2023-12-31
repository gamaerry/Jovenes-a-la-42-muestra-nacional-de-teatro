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
import gamaerry.jovenesala42muestranacionaldeteatro.guardados
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListaGuardadosFragment : ListaFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regresarEstadoPredeterminado()
        viewModelPrincipal.setEnGuardados(true)

        // de aqui es donde el otro adapter
        // consigue en tiempo real la listaGuardada
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.listaGuardados.collect { listaGuardados ->
                    if (viewModelPrincipal.enGuardados.value)
                        (requireActivity() as MainActivity).setContador("${listaGuardados.size} resultados")
                    if (viewModelPrincipal.ordenadosPorNombre)
                        profesionalesAdapter.submitList(listaGuardados.sortedBy { it.nombre })
                    else
                        profesionalesAdapter.submitList(listaGuardados.sortedBy { it.id })
                    if (listaGuardados.isEmpty()) {
                        binding.textoDeFondo.text = if (requireActivity().guardados!!.isEmpty())
                            "Lista de profesionales guardados vacía."
                        else
                            "No hay profesionales que cumplan con los filtros de búsqueda."
                        binding.textoDeFondo.visibility = View.VISIBLE
                        binding.miRecyclerView.visibility = View.GONE
                    } else {
                        binding.textoDeFondo.text = ""
                        binding.textoDeFondo.visibility = View.GONE
                        binding.miRecyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }

        // se consigue de la propiedad esLineal del viewModel
        // para establecer el acomodo de la lista adecuado
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.guardadosEsLineal.collect {
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

        // se define que va a pasar con el icono
        // que se encarga del guardado de profesionales
        binding.guardado.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_unarchive
            )
        )
    }
}