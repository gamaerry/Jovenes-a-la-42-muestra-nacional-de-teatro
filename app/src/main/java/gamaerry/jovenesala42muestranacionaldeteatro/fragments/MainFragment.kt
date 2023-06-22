package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentListaProfesionalesBinding
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    // los binding enlazan a las vistas con el codigo
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navegacion.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> requireActivity().supportFragmentManager.popBackStack()
                R.id.guardados -> requireActivity().supportFragmentManager.beginTransaction()
                    // usa estas animaciones ya definidas
                    .setCustomAnimations(
                        R.anim.entrar_desde_derecha,
                        R.anim.salir_hacia_izquierda,
                        R.anim.entrar_desde_izquierda,
                        R.anim.salir_hacia_derecha
                    )
                    // reemplaza (no agrega) el DetallesProfesionalesFragment
                    .replace(R.id.contenedorDeListas, ListaGuardadosFragment())
                    // se guarda con la etiqueta correspondiente
                    .addToBackStack("listaGuardados")
                    .commit()
            }
            true
        }
    }

    // se sobreescriben estos metodos unicamente para
    // el correcto funcionamiento del nuestro binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}