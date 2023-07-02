package gamaerry.jovenesala42muestranacionaldeteatro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.ListaFiltrosAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ActivityMainBinding
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaGuardadosFragment
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaInicioFragment
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    @Inject
    lateinit var listaFiltrosAdapter: ListaFiltrosAdapter
    // uso un solo viewModel para todas las operaciones de la base de datos
    private val viewModelPrincipal: ViewModelPrincipal by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // con esta condicion se asegura de que se llame una sola vez
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedorPrincipal, ListaInicioFragment()).commit()

        // se manejan los eventos de la navegacion
        // haciendo uso del mecanismo backStack de Android
        binding.navegacion.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.inicio -> supportFragmentManager.popBackStack()
                R.id.guardados -> supportFragmentManager.beginTransaction()
                    // usa estas animaciones ya definidas
                    .setCustomAnimations(
                        R.anim.entrar_desde_derecha,
                        R.anim.salir_hacia_izquierda,
                        R.anim.entrar_desde_izquierda,
                        R.anim.salir_hacia_derecha
                    )
                    // reemplaza (no agrega) el DetallesProfesionalesFragment
                    .replace(R.id.contenedorPrincipal, ListaGuardadosFragment())
                    // se guarda con la etiqueta correspondiente
                    .addToBackStack("listaGuardados")
                    .commit()
            }
            true
        }

        // se inicializa el navigationView de configuraciones
        // Establecer el adaptador en el ExpandableListView
        binding.filtros.setAdapter(listaFiltrosAdapter)

        binding.filtros.setOnGroupClickListener { _, _, _, _ -> false }

        binding.filtros.setOnChildClickListener { parent, view, groupPosition, childPosition, id ->
            // Acción al hacer clic en un elemento secundario
            // Realiza la acción deseada para el elemento seleccionado
            true
        }
        binding.orden.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.porNombre -> viewModelPrincipal.ordenadosPorNombre = true
                R.id.porEstado -> viewModelPrincipal.ordenadosPorNombre = false
            }
            viewModelPrincipal.reordenar()
        }
    }

    fun setItemNavegacionInicio(){
        binding.navegacion.selectedItemId = R.id.inicio
    }

    fun aparecerNavegacion() {
        binding.navegacion.visibility = View.VISIBLE
    }

    fun desaparecerNavegacion() {
        binding.navegacion.visibility = View.GONE
    }
}