package gamaerry.jovenesala42muestranacionaldeteatro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamaerry.jovenesala42muestranacionaldeteatro.data.RepositorioPrincipal
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
constructor(private val repositorio: RepositorioPrincipal) : ViewModel() {
    // aqui es donde se almacenan a los profesionales del reciclerView
    private val _listaProfesionalesDeTeatro = MutableStateFlow<List<ProfesionalDelTeatro>>(emptyList())
    val listaProfesionalesDeTeatro: StateFlow<List<ProfesionalDelTeatro>> get() = _listaProfesionalesDeTeatro

    // aqui se almacena al profesional a mostrar
    private val _profesionalEnfocado = MutableStateFlow<ProfesionalDelTeatro?>(null)
    val profesionalEnfocado: StateFlow<ProfesionalDelTeatro?> get() = _profesionalEnfocado

    // aqui es donde se almacena la listaDeNotas
    private val _esLineal = MutableStateFlow(false)
    val esLineal: StateFlow<Boolean> get() = _esLineal

    // cambia el valor del acomodo y lo regresa
    fun switchEsLineal(): Boolean {
        _esLineal.value = !_esLineal.value
        return esLineal.value
    }

    // establece al profesional seleccionado en su variable correspondiente
    fun setProfesionalEnfocado(profesional: ProfesionalDelTeatro) {
        _profesionalEnfocado.value = profesional
    }

    // a partir de las palabras clave realiza la busqueda de los
    // profesionales a mostrar (notese que si palabrasClave es ""
    // entonces mostrara a todos los profesionales de la base de datos)
    fun getProfesionales() {
        repositorio.getListaDeProfesionales("").onEach {
            _listaProfesionalesDeTeatro.value = it
        }.launchIn(viewModelScope)
    }
}