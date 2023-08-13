package gamaerry.jovenesala42muestranacionaldeteatro

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import gamaerry.jovenesala42muestranacionaldeteatro.adapters.ListaFiltrosAdapter
import gamaerry.jovenesala42muestranacionaldeteatro.databinding.ActivityMainBinding
import gamaerry.jovenesala42muestranacionaldeteatro.datastructure.RepositorioPrincipal
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.EditarDetallesFragment
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaFragment
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaGuardadosFragment
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.ListaInicioFragment
import gamaerry.jovenesala42muestranacionaldeteatro.fragments.LoginFragment
import gamaerry.jovenesala42muestranacionaldeteatro.viewmodel.ViewModelPrincipal
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // el objeto binding es el enlace entre el codigo y las vistas
    private lateinit var binding: ActivityMainBinding

    // los expandableListView necesitan su adapter asi como los recyclerView
    // (notese que este clase es Singleton y no necesita estar en el modulo)
    @Inject
    lateinit var listaFiltrosAdapter: ListaFiltrosAdapter

    @Inject
    lateinit var repositorioPrincipal: RepositorioPrincipal

    // uso un solo viewModel (Singleton) para todas las operaciones de la base de datos
    private val viewModelPrincipal: ViewModelPrincipal by viewModels()

    // no es necesario sobreescribir mas que este método que ejecuta
    // a la funcion privada inicio() solo una vez al abrir la app
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // se infla nuestro binding con las vistas correspondientes
        binding = ActivityMainBinding.inflate(layoutInflater)
        // establece en el binding en el Activity
        setContentView(binding.root)
        // con esta condicion se asegura de que se llame una sola vez
        if (savedInstanceState == null)
            inicio()
        // establece el comportamiento de cada vista
        // (en cada creacion de la activity -y por
        // lo tanto en cada cambio de configuracion-)
        // de acuerdo a la logica requerida
        setComportamientos()
        // establece el nombre del usuario para el
        // saludo en cada cambio de configuración
        setSaludo()
    }

    private fun setComportamientos() {
        // se manejan los eventos de la navegacion
        // haciendo uso del mecanismo backStack de Android
        binding.navegacion.setOnItemSelectedListener {
            when (it.itemId) {
                // puesto que "inicio" a diferencia de "guardados" no se agrega al
                // backStack solo se inicia su transicion, presionar "inicio" desde
                // "guardados" equivale solo a quitar el fragmento actual del backStack
                // para regresar al primero (notese que si se presiona desde el mismo
                // inicio no termina la actividad porque el backStack esta vacio)
                R.id.inicio -> supportFragmentManager.popBackStack()
                R.id.guardados ->
                    // la condicion es para que al presionar "guardados" se haga una transicion
                    // solo cuando el backstack este vacio y evitar instanciar mas de un "guardados"
                    if (supportFragmentManager.backStackEntryCount == 0)
                        supportFragmentManager.beginTransaction()
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
                            // ejecuta la transicion
                            .commit()
            }
            true
        }

        // establecer el adaptador en el ExpandableListView
        binding.filtros.setAdapter(listaFiltrosAdapter.apply {
            // esta funcion lambda es escencial para que el cambio de los estados
            // check de los filtros actualicen a las listas, ademas de limpiar la
            // busqueda para que no interfiera con el filtrado (notese que el orden importa)
            actualizarLista = {
                (supportFragmentManager.fragments[0] as ListaFragment).limpiarBusqueda()
                actualizarListas()
            }
        })

        // este listener es necesario para que se "expanda" el expandableListView
        binding.filtros.setOnGroupClickListener { _, _, _, _ -> false }

        // el viewModel se encarga de ordenar las vistas, el radioGroup solo
        // cambia el estado de la variable publica booleana ordenadosPorNombre
        binding.orden.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.porNombre -> viewModelPrincipal.ordenadosPorNombre = true
                R.id.porEstado -> viewModelPrincipal.ordenadosPorNombre = false
            }
            // ejecuta el reordenamiento que corresponde al valor de ordenadosPorNombre
            viewModelPrincipal.reordenar()
        }

        // el boton reestablecer filtros establece en true todos los checkBox,
        // contrae (colapsa) cada filtro, además de hacer un filtrado de nueva
        // cuenta para actualizar las listas con todos los filtros activados
        binding.restablecerFiltros.setOnClickListener {
            restablecerExpandableListView()
            viewModelPrincipal.filtrarListas()
            // (filtrarListas dentro del restablecerExpandableListView
            // provocaba una busqueda erronea, ya que al buscar tambien
            // se necesita restablecerExpandableListView pero no filtrar)
        }
        binding.editarDetalles.setOnClickListener {
            if (nombre != null) {
                binding.drawer.closeDrawer(GravityCompat.START)
                viewModelPrincipal.updateUsuario()
                supportFragmentManager.beginTransaction()
                    // reemplaza (no agrega) el DetallesProfesionalesFragment
                    .replace(R.id.contenedorPrincipal, EditarDetallesFragment())
                    // se guarda con la etiqueta correspondiente
                    .addToBackStack("editarDetalles")
                    // ejecuta la transicion
                    .commit()
            } else Toast.makeText(
                this,
                "Para salir de la sesión de invitado reinicie la app",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun setContador(resultados: String) {
        binding.contador.text = resultados
    }

    private fun inicio() {
        // si el usuario todavia no se ha ingresado, se lanza el fragmento
        // del login y si ya esta ingresado y establecido en la variable 
        // usuario del activity entonces se inicia con la transicion de la listaInicio
        if (nombre == null)
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedorPrincipal, LoginFragment()).commit()
        //(notese que no se puede establecer el saludo aun porque usuario es null)
        else {
            supportFragmentManager.beginTransaction()
                .add(R.id.contenedorPrincipal, ListaInicioFragment()).commit()
        }
        // es necesaria la actualizacion de ambas listas en este inicio
        // (notese que las listas se actualizan aun desde el login)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                repositorioPrincipal.listaCompleta.collect {
                    actualizarListas()
                }
            }
        }
        actualizarListas()
    }

    fun setSaludo() {
        // cambia el saludo correspondiente del navigationView
        nombre?.let { binding.saludo.text = getString(R.string.saludos, it) }
    }

    private fun actualizarListas() {
        // para actualizar ambas listas es necesario
        // pasar la lista actual de guardados
        viewModelPrincipal.updateListas(guardados!!)
    }

    fun restablecerExpandableListView() {
        // cierra (colapsa) a cada filtro y los reestablece
        // (el cerrado es necesario porque al cambiar los
        // estados de check programaticamente el cambio visual
        // no se aplica hasta un "cambio de configuracion")
        for (i in getFiltros().indices)
            binding.filtros.collapseGroup(i)
        viewModelPrincipal.restablecerFiltros()
    }

    fun setItemNavegacionInicio() {
        // esta funcion publica la utiliza ListaInicioFragment
        // cada vez que se crea para cambiar visualmente
        // a inicio en el bottomNavigationView
        binding.navegacion.selectedItemId = R.id.inicio
    }

    fun aparecerNavegacion() {
        // el bottomNavigationView debe aparecer en
        // cualquiera de las dos listas de profesionales
        binding.navegacion.visibility = View.VISIBLE
    }

    fun desaparecerNavegacion() {
        // el bottomNavigationView debe desaparecer en el
        // login y en los detalles de los profesionales
        binding.navegacion.visibility = View.GONE
    }

    fun aparecerConfiguracion() {
        // para poder hacer la aparicion del navigationView establecemos
        // su gravedad a la izquierda de la pantalla y lo hacemos visible
        binding.configuracion.updateLayoutParams {
            (this as DrawerLayout.LayoutParams).gravity = GravityCompat.START
        }
        binding.configuracion.visibility = View.VISIBLE
    }

    fun desaparecerConfiguracion() {
        // para poder hacer la aparicion del navigationView
        // reseteamos su gravedad y lo hacemos invisible
        binding.configuracion.updateLayoutParams {
            (this as DrawerLayout.LayoutParams).gravity = 0
        }
        binding.configuracion.visibility = View.GONE
    }

    fun enviarCorreo(correo: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            putExtra(Intent.EXTRA_EMAIL, arrayOf(correo))
            data = Uri.parse("mailto:")
        }
        startActivity(Intent.createChooser(intent, "Elige la aplicación de correo"))
    }

    fun abrirPerfilFacebook(facebook: String) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("fb://facewebmodal/f?href=$facebook")
        }
        startActivity(intent)
    }
}