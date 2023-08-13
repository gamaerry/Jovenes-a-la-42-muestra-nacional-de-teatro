package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialContainerTransform
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.MainActivity
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentEditarDetallesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.extraerLista
import gamaerry.jovenesala42muestranacionaldeteatro.extraerString
import gamaerry.jovenesala42muestranacionaldeteatro.getEspecialidades
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal

@AndroidEntryPoint
class EditarDetallesFragment : Fragment() {
    private var _binding: FragmentEditarDetallesBinding? = null
    private val binding get() = _binding!!
    private val viewModelPrincipal: ViewModelPrincipal by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animacion = MaterialContainerTransform().apply {
            duration = 300L
            scrimColor = Color.TRANSPARENT
        }
        sharedElementEnterTransition = animacion
        sharedElementReturnTransition = animacion
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarDetallesBinding.inflate(inflater, container, false)
        val usuario = viewModelPrincipal.usuario.value!!
        binding.nombre.setText(usuario.nombre)
        binding.descripcion.setText(usuario.descripcion)
        binding.email.setText(usuario.contacto1)
        binding.facebook.setText(usuario.contacto2)
        val listaIndicesEspecialidades = usuario.especialidades
            .extraerLista().map { getEspecialidades().indexOf(it) }
        listaIndicesEspecialidades.forEach { (binding.chips[it] as Chip).isChecked = true }
        binding.guardar.setOnClickListener {
            val listaEspecialidades = mutableListOf<String>()
            binding.chips.forEachIndexed { i, chip ->
                if ((chip as Chip).isChecked) listaEspecialidades.add(getEspecialidades()[i])
            }
            Firebase.database.reference
                .child("profesionales")
                .child(usuario.id.toString())
                .setValue(
                    usuario.copy(
                        nombre = binding.nombre.text.toString(),
                        especialidades = listaEspecialidades.extraerString(),
                        descripcion = binding.descripcion.text.toString()
                    )
                )
            Toast.makeText(
                requireActivity(),
                "Datos almacenados con éxito",
                Toast.LENGTH_SHORT
            ).show()
            requireActivity().supportFragmentManager.popBackStack()
        }

        (requireActivity() as MainActivity).desaparecerNavegacion()
        (requireActivity() as MainActivity).desaparecerConfiguracion()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).aparecerNavegacion()
        (requireActivity() as MainActivity).aparecerConfiguracion()
        _binding = null
    }
}