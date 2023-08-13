package gamaerry.jovenesala42muestranacionaldeteatro.fragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.MainActivity
import gamaerry.jovenesala42muestranacionaldeteatro.R
import gamaerry.jovenesala42muestranacionaldeteatro.clearGuardados
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.FragmentLoginBinding
import gamaerry.jovenesala42muestranacionaldeteatro.guardados
import gamaerry.jovenesala42muestranacionaldeteatro.setId
import gamaerry.jovenesala42muestranacionaldeteatro.setNombre
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    // los binding enlazan a las vistas con el codigo
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    // uso un solo viewModel para todas las operaciones de la base de datos
    private val viewModelPrincipal: ViewModelPrincipal by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // al estar cargadas las vistas se establece que el boton:
        binding.botonEntrar.setOnClickListener {
            if (hayConexion(requireActivity())) {
                // guarde en una variable el id numerico introducido
                val id = binding.campoId.text.toString()
                Firebase.database.apply {
                    getReference("ids").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            dataSnapshot.getValue<String>()?.let {
                                if (id.isNotEmpty())
                                    viewModelPrincipal.setUsuario(
                                        if (id.length == 10 && it.contains(id))
                                            id.substring(0..5).toInt()
                                        else 999999
                                    )
                                else Toast.makeText(
                                    requireActivity(),
                                    "Por favor, introduzca su id asignado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.d("midebug", "Algo ha fallado :(")
                        }
                    })
                }
            } else Toast.makeText(
                requireActivity(),
                "Compruebe su conexión a internet",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.botonEntrarComoInvitado.setOnClickListener {
            if (hayConexion(requireActivity()))
                (requireActivity() as MainActivity).apply {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.contenedorPrincipal, ListaInicioFragment()).commit()
                }
            else Toast.makeText(
                requireActivity(),
                "Compruebe su conexión a internet",
                Toast.LENGTH_SHORT
            ).show()
        }

        // cada que cambie el String usuario del viewModel se establece:
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModelPrincipal.usuario.collect {
                    // si el nombre es vacio, entonces quiere decir que la operacion setUsuario
                    // no encontro ninguna coincidencia en la base de datos y por lo tanto
                    // se le especifica al usuario ademas de resetear el campo del Id
                    it?.let {
                        if (it.nombre.isEmpty()) {
                            Toast.makeText(
                                requireActivity(),
                                "Id no válido, intente de nuevo",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.campoId.text.clear()
                        } else {
                            // una vez encontrado a algun usuario no nulo es necesario
                            // reforzar la siguiente operacion verificando si no es vacio,
                            // en cuyo caso, se realiza la transicion mediante el requireActivity,
                            // se le establece su correspondiente usuario y saludo
                            (requireActivity() as MainActivity).apply {
                                setNombre(it.nombre)
                                setId(it.id)
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.contenedorPrincipal, ListaInicioFragment())
                                    .commit()
                                setSaludo()
                                guardados?.forEach { id -> viewModelPrincipal.removeGuardado(id.toInt()) }
                                clearGuardados()
                            }
                            // finalmente y por unica ocasion se le da la bienvenida
                            // al usuario (notese que no es lo mismo que el saludo)
                            Toast.makeText(
                                requireActivity(),
                                "¡Bienvenidx, ${it.nombre}!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun hayConexion(contexto: Context): Boolean {
        val manager = contexto.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        // Android 10+
            manager.getNetworkCapabilities(manager.activeNetwork)?.let { networkCapabilities ->
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
        else return manager.activeNetworkInfo?.isConnectedOrConnecting == true
        return false
    }

    // al igual que sucede con el fragmento detallesProfesionales
    // en cada creacion debe asegurarce la desaparicion de tanto
    // la navegacion inferior como el menu de configuracion
    // (notese que ademas se infla el binding con las vistas)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        (requireActivity() as MainActivity).desaparecerNavegacion()
        (requireActivity() as MainActivity).desaparecerConfiguracion()
        return binding.root
    }

    // al igual que sucede con el fragento detallesProfesionales
    // en cada destruccion debe asegurarse la aparicion de tanto
    // la navegacion inferior como el menu de configuracion
    // (notese que ademas se resetea el binding a nulo)
    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as MainActivity).aparecerNavegacion()
        (requireActivity() as MainActivity).aparecerConfiguracion()
        _binding = null
    }
}