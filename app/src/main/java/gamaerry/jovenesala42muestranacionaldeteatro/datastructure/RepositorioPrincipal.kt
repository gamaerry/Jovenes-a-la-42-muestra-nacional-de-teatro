package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import gamaerry.jovenesala42muestranacionaldeteatro.extraerLista
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

// la función del repositorio es únicamente emitir los
// resultados de las funciones suspendibles del dao
class RepositorioPrincipal(
    val listaCompleta: MutableStateFlow<List<ProfesionalDelTeatro>> = MutableStateFlow(emptyList())
) {
    // es basicamente la ejecucion de una consulta de sql
    fun getListaDeProfesionales(palabrasClave: String) = flow {
        emit(listaCompleta.value.filter {
            it.descripcion.contains(palabrasClave, true) &&
                    it.nombre.contains(palabrasClave, true) &&
                    it.estado.contains(palabrasClave, true) &&
                    it.especialidades.contains(palabrasClave, true)
        })
    }.catch { it.printStackTrace() }

    // es basicamente la ejecucion de una consulta de sql
    fun getNombrePorId(id: Int) = flow {
        emit(listaCompleta.value.first { it.id == id }.nombre)
    }.catch { it.printStackTrace() }

    // filtra por partes para obtener una consulta adecuada
    // de acuerdo a los estados especialidades y muestras activas
    fun getProfesionalesPorFiltros(
        estados: List<String>,
        especialidades: List<String>,
        muestra: List<String>
    ) = flow {
        // obtiene en primer lugar una lisa de profesionales de acuerdo a la lista de estados
        // luego de la lista anterior, filtra aquellos cuya alguna de su
        // especialida coincida con alguna de la lista de especialidades
        val filtro = listaCompleta.value.filter { it.estado in estados }.filter { profesional ->
            profesional.especialidades.extraerLista().any { it in especialidades }
        }
        // finalmente se emitira una lista vacia si esta desmarcada la unica muestra nacional
        // de teatro hasta ahora (puesto que por el momento todos los profesionales son de esta)
        emit(if (muestra.isEmpty()) emptyList() else filtro)
    }.catch { it.printStackTrace() }
}