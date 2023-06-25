package gamaerry.jovenesala42muestranacionaldeteatro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ActivityMainBinding
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaGuardadosFragment
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaInicioFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // con esta condicion se asegura de que se llame una sola vez
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedorPrincipal, ListaInicioFragment()).commit()

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