package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.forEachIndexed
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
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
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class EditarDetallesFragment : Fragment() {
    private var _binding: FragmentEditarDetallesBinding? = null
    private val binding get() = _binding!!
    private val viewModelPrincipal: ViewModelPrincipal by activityViewModels()
    private val redesSocialesValidadas = mutableListOf<Editable>()

    @Inject
    @JvmSuppressWildcards
    lateinit var validaciones: List<(String) -> Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animacion = MaterialContainerTransform().apply {
            duration = 300L
            scrimColor = Color.TRANSPARENT
        }
        sharedElementEnterTransition = animacion
        sharedElementReturnTransition = animacion
        requireActivity().apply {
            (this as MainActivity).ocultarCarga()
            onBackPressedDispatcher.addCallback(this@EditarDetallesFragment,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        MaterialAlertDialogBuilder(this@apply)
                            .setTitle("Advertencia")
                            .setMessage("¿Está seguro(a) de que quiere salir sin guardar los cambios?")
                            .setNegativeButton("Quedarse") { dialog, _ ->
                                dialog.dismiss()
                            }.setPositiveButton("Sí, salir") { dialog, _ ->
                                dialog.dismiss()
                                supportFragmentManager.popBackStack()
                            }.show()
                    }
                })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditarDetallesBinding.inflate(inflater, container, false)
        val usuario = viewModelPrincipal.usuario.value!!
        val redesSociales = listOf(
            binding.facebook,
            binding.email,
            binding.instagram,
            binding.tiktok,
            binding.wp
        )
        initCampos(usuario, redesSociales)
        binding.guardar.setOnClickListener {
            val mensaje = validarCampos(redesSociales)
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
                        descripcion = binding.descripcion.text.toString(),
                        contacto1 = try {
                            redesSocialesValidadas[0].toString()
                        } catch (_: Exception) {
                            "null"
                        },
                        contacto2 = try {
                            redesSocialesValidadas[1].toString()
                        } catch (_: Exception) {
                            "null"
                        },
                        contacto3 = try {
                            redesSocialesValidadas[2].toString()
                        } catch (_: Exception) {
                            "null"
                        }
                    )
                )
            }
        }
        (requireActivity() as MainActivity).desaparecerNavegacion()
        (requireActivity() as MainActivity).desaparecerConfiguracion()
        return binding.root
    }

    private fun initCampos(usuario: ProfesionalDelTeatro, redesSociales: List<TextInputEditText>) {
        binding.nombre.setText(usuario.nombre)
        binding.descripcion.setText(usuario.descripcion)
        redesSociales.forEachIndexed { i, campo ->
            if (validaciones[i](usuario.contacto1))
                campo.setText(usuario.contacto1)
            if (validaciones[i](usuario.contacto2))
                campo.setText(usuario.contacto2)
            if (validaciones[i](usuario.contacto3))
                campo.setText(usuario.contacto3)
        }
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

    private fun validarRedesSociales(redesSociales: List<TextInputEditText>): String {
        var noVacios = 0
        val mensajesDeError = listOf(
            "Url de Facebook no válido",
            "Dirección de correo no válido",
            "Url de Instagram no válido",
            "Url de Tiktok no válido",
            "Número telefónico no válido"
        )
        var mensaje = ""
        redesSociales.map { it.text }.forEachIndexed { i, editable ->
            if (!editable.isNullOrEmpty() || !editable.isNullOrBlank()) {
                noVacios++
                if (!validaciones[i](editable.toString()))
                    mensaje = mensajesDeError[i]
                else
                    redesSocialesValidadas.add(editable)
            }
        }
        if (noVacios > 3)
            mensaje = "Se permiten hasta 3 modos de contacto"
        if (mensaje.isNotEmpty())
            redesSocialesValidadas.clear()
        return mensaje
    }

    private fun validarCampos(redesSociales: List<TextInputEditText>): String {
        var mensaje = ""
        if (binding.nombre.text.isNullOrEmpty() || binding.nombre.text.isNullOrBlank())
            mensaje = "El nombre es obligatorio"
        if (binding.descripcion.text.isNullOrEmpty() || binding.descripcion.text.isNullOrBlank())
            mensaje = "La semblanza es obligatoria"
        binding.chips.checkedChipIds.ifEmpty {
            mensaje = "Seleccione al menos una especialidad"
        }
        mensaje.ifEmpty { mensaje = validarRedesSociales(redesSociales) }
        return mensaje
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).aparecerNavegacion()
        (requireActivity() as MainActivity).aparecerConfiguracion()
        _binding = null
    }
}