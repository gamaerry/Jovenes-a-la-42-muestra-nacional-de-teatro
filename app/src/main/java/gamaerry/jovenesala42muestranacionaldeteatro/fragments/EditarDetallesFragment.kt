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
import gamaerry.jovenesala42muestranacionaldeteatro.setNombre
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
        initCampos(usuario)
        binding.guardar.setOnClickListener {
            val mensaje = validarCampos()
            if (mensaje.isNotEmpty())
                Toast.makeText(requireActivity(), mensaje, Toast.LENGTH_SHORT).show()
            else {
                val listaEspecialidades = mutableListOf<String>()
                binding.chips.forEachIndexed { i, chip ->
                    if ((chip as Chip).isChecked) listaEspecialidades.add(getEspecialidades()[i])
                }
                guardar(
                    usuario.copy(
                        nombre = binding.nombre.text.toString(),
                        especialidades = listaEspecialidades.extraerString(),
                        descripcion = binding.descripcion.text.toString()
                    )
                )
            }
        }
        (requireActivity() as MainActivity).desaparecerNavegacion()
        (requireActivity() as MainActivity).desaparecerConfiguracion()
        return binding.root
    }

    private fun initCampos(usuario: ProfesionalDelTeatro) {
        binding.nombre.setText(usuario.nombre)
        binding.descripcion.setText(usuario.descripcion)
        binding.email.setText(usuario.contacto1)
        binding.facebook.setText(usuario.contacto2)
        val listaIndicesEspecialidades = usuario.especialidades
            .extraerLista().map { getEspecialidades().indexOf(it) }
        listaIndicesEspecialidades.forEach { (binding.chips[it] as Chip).isChecked = true }
    }

    private fun guardar(usuarioModificado: ProfesionalDelTeatro) {
        Firebase.database.reference
            .child("profesionales")
            .child(usuarioModificado.id.toString())
            .setValue(usuarioModificado)
        (requireActivity() as MainActivity).apply {
            setNombre(binding.nombre.text.toString())
            setSaludo()
            supportFragmentManager.popBackStack()
            Toast.makeText(
                this,
                "Datos almacenados con éxito",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun masDeTresContactos(): Boolean {
        var i = 0
        if (binding.facebook.text.isNullOrEmpty() || binding.facebook.text.isNullOrBlank()) i++
        if (binding.email.text.isNullOrEmpty() || binding.email.text.isNullOrBlank()) i++
        if (binding.instagram.text.isNullOrEmpty() || binding.instagram.text.isNullOrBlank()) i++
        if (binding.tiktok.text.isNullOrEmpty() || binding.tiktok.text.isNullOrBlank()) i++
        if (binding.wp.text.isNullOrEmpty() || binding.wp.text.isNullOrBlank()) i++
        return i > 3
    }

    private fun validarCampos(): String {
        var mensaje = ""
        if (binding.nombre.text.isNullOrEmpty() || binding.nombre.text.isNullOrBlank())
            mensaje = "El nombre es obligatorio"
        if (binding.descripcion.text.isNullOrEmpty() || binding.nombre.text.isNullOrBlank())
            mensaje = "La semblanza es obligatoria"
        binding.chips.checkedChipIds.ifEmpty {
            mensaje = "Seleccione al menos una especialidad"
        }
        if (masDeTresContactos())
            mensaje = "Se permiten hasta 3 modos de contacto"
        return mensaje
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).aparecerNavegacion()
        (requireActivity() as MainActivity).aparecerConfiguracion()
        _binding = null
    }
}