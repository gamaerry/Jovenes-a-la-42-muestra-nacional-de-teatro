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
import coil.load
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.EspecialidadesAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentDetallesProfesionalesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.extraerLista
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetallesProfesionalesFragment : Fragment() {
    private var _binding: FragmentDetallesProfesionalesBinding? = null
    private val binding get() = _binding!!
    @Inject
    lateinit var especialidadesAdapter: EspecialidadesAdapter
    private val viewModelPrincipal: ViewModelPrincipal by activityViewModels()
    private val accionAlCambiarProfesionalEnfocado: (ProfesionalDelTeatro?) -> Unit = {
        binding.nombre.text = it?.nombre
        binding.descripcion.text = it?.descripcion
        binding.imagen?.load(it?.urlImagen)
        especialidadesAdapter.listaDeEspecialidades = it?.especialidades!!.extraerLista()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.botonesDeEspecialidades.apply {
            layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP)
            adapter = especialidadesAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.profesionalEnfocado.collect {
                    accionAlCambiarProfesionalEnfocado(it)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetallesProfesionalesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}