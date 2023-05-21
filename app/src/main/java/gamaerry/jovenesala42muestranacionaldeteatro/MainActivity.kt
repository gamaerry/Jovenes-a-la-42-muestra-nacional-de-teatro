package gamaerry.jovenesala42muestranacionaldeteatro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.core.view.isGone
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ActivityMainBinding
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaProfesionalesFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // EN CADA CAMBIO DE CONFIGURACIÓN SUCEDE ESTO:
        update()
        onBackPressedDispatcher.addCallback(this) {
            when (supportFragmentManager.backStackEntryCount) {
                1 -> finish()// cuando hay solo un fragmento en la actividad es cuando finaliza
                2 -> {// es cuando hay dos (el del menu principal y el 1er fragmento del modulo)
                    supportFragmentManager.popBackStack()
                    mostrarNavigationView()
                }
                else -> supportFragmentManager.popBackStack()
            }
        }
        // PERO ESTO SOLO AL INICIO:
        if (savedInstanceState == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedorPrincipal, ListaProfesionalesFragment()).addToBackStack("listaProfesionales").commit()
    }

    private fun update() {
        if (supportFragmentManager.backStackEntryCount > 1)
            ocultarNavigationView()
        else mostrarNavigationView()
    }

    private fun mostrarNavigationView() {
        binding.navegacion.isGone = false
    }

    fun ocultarNavigationView() {
        binding.navegacion.isGone = true
    }
}