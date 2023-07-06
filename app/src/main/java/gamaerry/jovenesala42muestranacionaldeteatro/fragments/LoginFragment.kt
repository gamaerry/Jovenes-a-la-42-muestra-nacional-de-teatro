package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.MainActivity
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentLoginBinding
import gamaerry.jovenesala42muestranacionaldeteatro.setUsuario
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModelPrincipal: ViewModelPrincipal by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.botonEntrar.setOnClickListener {
            val id = binding.campoId.text.toString()
            if (id.isEmpty())
                Toast.makeText(
                    requireActivity(),
                    "Por favor, introduzca su id asignado",
                    Toast.LENGTH_SHORT
                ).show()
            else viewModelPrincipal.setUsuario(id.toInt())
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.usuario.collect {
                    if (it == null) {
                        Toast.makeText(
                            requireActivity(),
                            "Id no válido, intente de nuevo",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.campoId.text.clear()
                    } else if (it.isNotEmpty()) {
                        (requireActivity() as MainActivity).apply {
                            setUsuario(it)
                            supportFragmentManager.beginTransaction()
                                .replace(R.id.contenedorPrincipal, ListaInicioFragment()).commit()
                            actualizarListas()
                            setSaludo()
                        }
                        Toast.makeText(
                            requireActivity(),
                            "¡Bienvenidx, ${it}!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).desaparecerNavegacion()
        (requireActivity() as MainActivity).desaparecerConfiguracion()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).aparecerNavegacion()
        _binding = null
    }
}