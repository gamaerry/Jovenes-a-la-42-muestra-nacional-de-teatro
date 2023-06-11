package gamaerry.jovenesala42muestranacionaldeteatro.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gamaerry.jovenesala42muestranacionaldeteatro.data.RepositorioPrincipal
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.model.getProfesionalesDePrueba
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ProfesionalesViewModel
@Inject
constructor(private val repositorio: RepositorioPrincipal) : ViewModel() {
    private val _listaProfesionalesDeTeatro = MutableStateFlow<List<ProfesionalDelTeatro>>(emptyList())
    val listaProfesionalesDeTeatro: StateFlow<List<ProfesionalDelTeatro>> get() = _listaProfesionalesDeTeatro

    private val _profesionalEnfocado = MutableStateFlow<ProfesionalDelTeatro?>(null)
    val profesionalEnfocado: StateFlow<ProfesionalDelTeatro?> get() = _profesionalEnfocado

    private val _esLineal = MutableStateFlow(false)
    val esLineal: StateFlow<Boolean> get() = _esLineal

    fun switchEsLineal(): Boolean {
        _esLineal.value = !_esLineal.value
        return esLineal.value
    }

    fun setProfesionalEnfocado(profesional: ProfesionalDelTeatro) {
        _profesionalEnfocado.value = profesional
    }

    fun getProfesionales() {
        repositorio.getListaDeProfesionales("").onEach {
            _listaProfesionalesDeTeatro.value = it
        }.launchIn(viewModelScope)
    }
}