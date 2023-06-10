package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.ListaProfesionalesAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentListaProfesionalesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ProfesionalesDelTeatroViewModel
import kotlinx.coroutines.launch

class ListaProfesionalesFragment : Fragment() {
    private var _binding: FragmentListaProfesionalesBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val profesionalesViewModel: ProfesionalesDelTeatroViewModel by activityViewModels()
        val profesionalesAdapter = ListaProfesionalesAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                profesionalesViewModel.listaProfesionalesDeTeatro.collect {
                    profesionalesAdapter.submitList(it)
                }
            }
        }

        profesionalesAdapter.accionAlHacerClic = {
            // aqui solo se manda una funcion, mas el objeto
            // ProfesionalDelTeatro lo consigue  en el lifecycleScope
            profesionalesViewModel.setProfesionalEnfocado(it)
            requireActivity().supportFragmentManager.beginTransaction().apply {
                setCustomAnimations(
                    R.anim.entrar_desde_derecha,
                    R.anim.salir_hacia_izquierda,
                    R.anim.entrar_desde_izquierda,
                    R.anim.salir_hacia_derecha
                )
                replace(R.id.contenedorPrincipal, DetallesProfesionalesFragment())
                addToBackStack("detallesProfesionales")
                commit()
            }
        }

        binding.miRecyclerView.apply {
            val cuadricula = GridLayoutManager(requireContext(), 2)
            cuadricula.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int =
                    if (position == 0) 2 // Tamaño completo para la cabecera en la posición 0
                    else 1 // Tamaño normal para los elementos regulares
            }
            layoutManager = cuadricula
            adapter = profesionalesAdapter
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