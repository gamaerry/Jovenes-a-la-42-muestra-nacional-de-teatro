package gamaerry.jovenesala42muestranacionaldeteatro.viewmodel

import androidx.lifecycle.ViewModel
import gamaerry.jovenesala42muestranacionaldeteatro.data.ProfesionalDelTeatro
import gamaerry.jovenesala42muestranacionaldeteatro.data.getProfesionalesDelTeatro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProfesionalesDelTeatroViewModel: ViewModel() {
    private val _listaProfesionalesDeTeatro = MutableStateFlow(getProfesionalesDelTeatro())
    val listaProfesionalesDeTeatro: StateFlow<List<ProfesionalDelTeatro>> get() = _listaProfesionalesDeTeatro
    private val _profesionalEnfocado = MutableStateFlow<ProfesionalDelTeatro?>(null)
    val profesionalEnfocado: StateFlow<ProfesionalDelTeatro?> get() = _profesionalEnfocado
    fun setProfesionalEnfocado(profesional: ProfesionalDelTeatro){
        _profesionalEnfocado.value= profesional
    }
}