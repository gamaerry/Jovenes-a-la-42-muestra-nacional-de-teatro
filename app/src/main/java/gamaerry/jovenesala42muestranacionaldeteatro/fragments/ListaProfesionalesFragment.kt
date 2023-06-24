package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.MainActivity
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListaProfesionalesFragment : ListaFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        regresarEstadoPredeterminado()

        // esta linea se ocupa cuando de Guardados se hace un popStack a Inicio
        // que por defecto no cambia el item de navegacion correspondiente
        (requireActivity() as MainActivity).setItemNavegacionInicio()

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

        // se define que va a pasar con el icono
        // que se encarga del guardado de profesionales
        binding.guardado.setOnClickListener { accionDelGuardado(false) }

        // cuando se presiona el item necesitamos enfocar dicho profesional
        // y realizar la transicion al DetallesProfesionalesFragment
        profesionalesAdapter.accionAlPresionar = { profesionalDelTeatro, itemView ->
            viewModelPrincipal.setProfesionalEnfocado(profesionalDelTeatro)
            getTransicion(itemView).commit()
        }

        // se selecciona al cardView al mantener presionado
        profesionalesAdapter.accionAlPresionarLargo = { seleccionar(it)}

        // OnQueryTextListener es una interfaz que requiere la
        // implementacion de dos métodos, uno para cuando cambia el
        // String de busqueda y otro para cuando se da al boton de buscar
        binding.busqueda.setOnQueryTextListener(buscar(view, false))
    }
}