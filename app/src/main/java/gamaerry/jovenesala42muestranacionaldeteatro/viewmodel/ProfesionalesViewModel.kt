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
import javax.inject.Inject

@HiltViewModel
class ProfesionalesViewModel
@Inject
constructor(private val repositorio: RepositorioPrincipal) : ViewModel() {
    private val _listaProfesionalesDeTeatro = MutableStateFlow(getProfesionalesDePrueba())
    val listaProfesionalesDeTeatro: StateFlow<List<ProfesionalDelTeatro>> get() = _listaProfesionalesDeTeatro

    private val _profesionalEnfocado = MutableStateFlow<ProfesionalDelTeatro?>(null)
    val profesionalEnfocado: StateFlow<ProfesionalDelTeatro?> get() = _profesionalEnfocado

    private val _esLineal = MutableStateFlow(false)
    val esLineal: StateFlow<Boolean> get() = _esLineal

    init {
        _listaProfesionalesDeTeatro.value.forEach {
            repositorio.insertarProfesional(it).launchIn(viewModelScope)
        }
    }

    fun switchEsLineal(): Boolean{
        _esLineal.value = !_esLineal.value
        return esLineal.value
    }

    fun setProfesionalEnfocado(profesional: ProfesionalDelTeatro) {
        _profesionalEnfocado.value = profesional
    }
}