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
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.EspecialidadesAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentDetallesProfesionalesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.extraerLista
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ProfesionalesDelTeatroViewModel
import kotlinx.coroutines.launch

class DetallesProfesionalesFragment : Fragment() {
    private var _binding: FragmentDetallesProfesionalesBinding? = null
    private val binding get() = _binding!!
    private val accionAlCambiarProfesionalEnfocado: (ProfesionalDelTeatro?) -> Unit = {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val profesionalesViewModel: ProfesionalesDelTeatroViewModel by activityViewModels()
        val especialidadesAdapter = EspecialidadesAdapter()
        super.onViewCreated(view, savedInstanceState)
        binding.botonesDeEspecialidades.apply {
            layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP)
            adapter = especialidadesAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                profesionalesViewModel.profesionalEnfocado.collect {
                    binding.nombre.text = it?.nombre
                    binding.descripcion.text = it?.descripcion
                    binding.imagen?.load(it?.urlImagen)
                    especialidadesAdapter.listaDeEspecialidades = it?.especialidades!!.extraerLista() }
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