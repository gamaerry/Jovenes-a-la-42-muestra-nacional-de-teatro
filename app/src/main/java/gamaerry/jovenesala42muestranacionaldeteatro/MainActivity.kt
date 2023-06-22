package gamaerry.jovenesala42muestranacionaldeteatro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ActivityMainBinding
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaProfesionalesFragment
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.MainFragment

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // con esta condicion se asegura que ambos
        // FragmentContainerView se llenen una sola vez
        if (savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedorPrincipal, MainFragment()).commit()
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedorDeListas, ListaProfesionalesFragment()).commit()
        }
    }
}