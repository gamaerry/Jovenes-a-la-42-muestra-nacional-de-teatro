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
    private val idsGuardados = mutableSetOf<Int>()

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
    private val _usuario = MutableStateFlow<ProfesionalDelTeatro?>(null)
    val usuario: StateFlow<ProfesionalDelTeatro?> get() = _usuario

    // aqui es donde se almacena el tipo de acomodo
    private val _enGuardados = MutableStateFlow(false)
    val enGuardados: StateFlow<Boolean> get() = _enGuardados

    fun reordenar() {
        if (ordenadosPorNombre) {
            _listaInicio.value = listaInicio.value.sortedBy { it.nombre }
            _listaGuardados.value = listaGuardados.value.sortedBy { it.nombre }
        } else {
            _listaInicio.value = listaInicio.value.sortedBy { it.id }
            _listaGuardados.value = listaGuardados.value.sortedBy { it.id }
        }
    }

    fun updateUsuario(idUsuario: Int){
        _usuario.value = repositorio.listaCompleta.value.first { it.id == idUsuario }
    }

    // cambia el valor del acomodo y lo regresa
    fun switchInicioEsLineal() {
        _inicioEsLineal.value = !_inicioEsLineal.value
    }

    // cambia el valor del acomodo y lo regresa
    fun switchGuardadosEsLineal() {
        _guardadosEsLineal.value = !_guardadosEsLineal.value
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
    fun buscar(busqueda: String): Boolean {
        // es necesario actualizar en cada actualizacion de
        // palabras clave nuestra listaProfesionalesDeTeatro
        repositorio.getListaDeProfesionales(busqueda).onEach {
            if (enGuardados.value) // filtra de acuerdo a los ids guardados
                _listaGuardados.value = it.filter { profesional -> profesional.id in idsGuardados }
            else // no es necesario ningun filtrado en caso de tratarse del menu de inicio
                _listaInicio.value = it
        }.launchIn(viewModelScope)
        // devuelve true para indicar una busqueda exitosa
        return true
    }

    fun filtrarListas() {
        val estados = itemsFiltros[0].filterIndexed { i, _ -> estadosCheckBox[0][i] }
        val especialidades = itemsFiltros[1].filterIndexed { i, _ -> estadosCheckBox[1][i] }
        val muestra = itemsFiltros[2].filterIndexed { i, _ -> estadosCheckBox[2][i] }
        repositorio.getProfesionalesPorFiltros(estados, especialidades, muestra).onEach {
            _listaInicio.value = it
            _listaGuardados.value = it.filter { profesional -> profesional.id in idsGuardados }
        }.launchIn(viewModelScope)
    }

    private fun updateGuardados() {
        repositorio.getListaDeProfesionales("").onEach {
            _listaGuardados.value = it.filter { profesional -> profesional.id in idsGuardados }
        }.launchIn(viewModelScope)
    }

    fun restablecerFiltros() {
        estadosCheckBox.forEachIndexed { i, filtros ->
            filtros.forEachIndexed { j, _ ->
                estadosCheckBox[i][j] = true
            }
        }
    }

    fun setFiltroEspecialidad(especialidad: String) {
        estadosCheckBox[1].forEachIndexed { i, _ -> estadosCheckBox[1][i] = false }
        estadosCheckBox[1][itemsFiltros[1].indexOf(especialidad)] = true
        filtrarListas()
    }

    // a partir del id pasado se actualiza la listaGuardada
    fun addGuardado(id: String) {
        idsGuardados.add(id.toInt())
        updateGuardados()
    }

    // a partir del id pasado se actualiza la listaGuardada
    fun removeGuardado(id: Int) {
        idsGuardados.remove(id)
        updateGuardados()
    }

    fun updateListas(guardados: MutableSet<String>) {
        idsGuardados.addAll(guardados.map { it.toInt() })
        filtrarListas()
    }

    fun setUsuario(id: Int) {
        repositorio.getNombrePorId(id).onEach { _usuario.value = it }.launchIn(viewModelScope)
    }
}