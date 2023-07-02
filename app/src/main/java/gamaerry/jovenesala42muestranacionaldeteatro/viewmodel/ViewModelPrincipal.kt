package gamaerry.jovenesala42muestranacionaldeteatro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamaerry.jovenesala42muestranacionaldeteatro.datastructure.RepositorioPrincipal
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

// el ViewModelPrincipal es quiza la clase más importante del codigo, pues
// establece el enlace del repositorio (y sus operaciones) con el uso que
// se le de a los datos resultantes por parte de las vistas de los fragmentos
@HiltViewModel
class ViewModelPrincipal
@Inject
constructor(
    private val repositorio: RepositorioPrincipal,
    private val itemsFiltros: @JvmSuppressWildcards List<List<String>>,
    private val estadosCheckBox: @JvmSuppressWildcards List<MutableList<Boolean>>
) : ViewModel() {
    var ordenadosPorNombre = false

    // aqui es donde se almacenan a los profesionales del inicio
    private val _listaInicio = MutableStateFlow<List<ProfesionalDelTeatro>>(emptyList())
    val listaInicio: StateFlow<List<ProfesionalDelTeatro>> get() = _listaInicio

    // aqui es donde se almacenan a los profesionales guardados
    private val _listaGuardados = MutableStateFlow<List<ProfesionalDelTeatro>>(emptyList())
    val listaGuardados: StateFlow<List<ProfesionalDelTeatro>> get() = _listaGuardados

    // aqui se almacena al profesional a mostrar
    private val _profesionalEnfocado = MutableStateFlow<ProfesionalDelTeatro?>(null)
    val profesionalEnfocado: StateFlow<ProfesionalDelTeatro?> get() = _profesionalEnfocado

    // aqui es donde se almacena el tipo de acomodo
    private val _guardadosEsLineal = MutableStateFlow(false)
    val guardadosEsLineal: StateFlow<Boolean> get() = _guardadosEsLineal

    // aqui es donde se almacena el tipo de acomodo
    private val _inicioEsLineal = MutableStateFlow(false)
    val inicioEsLineal: StateFlow<Boolean> get() = _inicioEsLineal

    // aqui es donde se almacena el tipo de acomodo
    private val _enGuardados = MutableStateFlow(false)
    val enGuardados: StateFlow<Boolean> get() = _enGuardados

    // representa el filtrado de busqueda
    private val palabrasClave = MutableStateFlow("")

    init {
        setListaProfesionales()
    }

    fun reordenar() {
        if (ordenadosPorNombre) {
            _listaInicio.value = listaInicio.value.sortedBy { it.nombre }
            _listaGuardados.value = listaGuardados.value.sortedBy { it.nombre }
        } else {
            _listaInicio.value = listaInicio.value.sortedBy { it.id }
            _listaGuardados.value = listaGuardados.value.sortedBy { it.id }
        }
    }

    fun filtrar() {
        TODO("Not yet implemented")
    }

    // cambia el valor del acomodo y lo regresa
    fun switchInicioEsLineal(): Boolean {
        _inicioEsLineal.value = !_inicioEsLineal.value
        return inicioEsLineal.value
    }

    // cambia el valor del acomodo y lo regresa
    fun switchGuardadosEsLineal(): Boolean {
        _guardadosEsLineal.value = !_guardadosEsLineal.value
        return guardadosEsLineal.value
    }

    fun setEnGuardados(enGuardados: Boolean) {
        _enGuardados.value = enGuardados
    }

    // establece al profesional seleccionado en su variable correspondiente
    fun setProfesionalEnfocado(profesional: ProfesionalDelTeatro) {
        _profesionalEnfocado.value = profesional
    }

    // establece las palabras clave que devolveran la lista correspondiente en cada busqueda
    // (notese que si palabrasClave.value es "" room devolvera toda la base de datos)
    fun setPalabrasClave(busqueda: String): Boolean {
        palabrasClave.value = busqueda
        // es necesario actualizar en cada actualizacion de
        // palabras clave nuestra listaProfesionalesDeTeatro
        setListaProfesionales()
        // devuelve true para indicar una busqueda exitosa
        return true
    }

    // a partir de las palabras clave realiza la busqueda de los profesionales a mostrar
    private fun setListaProfesionales() {
        if (enGuardados.value) {
            // se guarda el estado actual de la listaGuardada para luego filtrar de la
            // busqueda en la base de datos unicamente aquellos que esten en listaGuardada
            repositorio.getListaDeProfesionales(palabrasClave.value).onEach {
                _listaGuardados.value =
                    it.filter { profesional -> listaGuardados.value.contains(profesional) }
            }.launchIn(viewModelScope)
        } else
        // no es necesario ningun filtrado en caso de tratarse del menu de inicio
            repositorio.getListaDeProfesionales(palabrasClave.value).onEach {
                _listaInicio.value = it
            }.launchIn(viewModelScope)
    }

    fun setListaPorEspecialidad(especialidad: String) {
        if (enGuardados.value) {
            repositorio.getProfesionalesPorEspecialidad(especialidad).onEach {
                _listaGuardados.value = it
            }.launchIn(viewModelScope)
        } else {
            repositorio.getProfesionalesPorEspecialidad(especialidad).onEach {
                _listaInicio.value = it
            }.launchIn(viewModelScope)
        }
    }

    // a partir del id pasado se actualiza la listaGuardada
    fun addGuardado(id: String) {
        repositorio.getProfesionalPorId(id).onEach {
            if (!listaGuardados.value.contains(it))
                _listaGuardados.value += it!!
        }.launchIn(viewModelScope)
    }

    // a partir del id pasado se actualiza la listaGuardada
    fun removeGuardado(id: Int) {
        _listaGuardados.value = _listaGuardados.value.filter { it.id != id }
    }
}