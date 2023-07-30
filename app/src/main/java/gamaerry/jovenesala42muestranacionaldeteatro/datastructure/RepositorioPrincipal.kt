package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import gamaerry.jovenesala42muestranacionaldeteatro.extraerLista
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// la función del repositorio es únicamente emitir los
// resultados de las funciones suspendibles del dao
@Singleton
class RepositorioPrincipal
@Inject
constructor(private val daoPrincipal: DaoPrincipal) {
    // es basicamente la ejecucion de una consulta de sql
    fun getListaDeProfesionales(palabrasClave: String) = flow {
        emit(daoPrincipal.operacionGetListaDeProfesionales(palabrasClave))
    }.catch { it.printStackTrace() }

    // es basicamente la ejecucion de una consulta de sql
    fun getNombrePorId(id: Int) = flow {
        emit(daoPrincipal.operacionGetNombrePorId(id))
    }.catch { it.printStackTrace() }

    // filtra por partes para obtener una consulta adecuada
    // de acuerdo a los estados especialidades y muestras activas
    fun getProfesionalesPorFiltros(
        estados: List<String>,
        especialidades: List<String>,
        muestra: List<String>
    ) = flow {
        // obtiene en primer lugar una lisa de profesionales de acuerdo a la lista de estados
        val filtroEstados = daoPrincipal.operacionGetProfesionalesPorEstados(estados)
        // luego de la lista anterior, filtra aquellos cuya alguna de su
        // especialida coincida con alguna de la lista de especialidades
        val filtroEspecialidades = filtroEstados.filter { profesional ->
            profesional.especialidades.extraerLista().any { it in especialidades }
        }
        // finalmente se emitira una lista vacia si esta desmarcada la unica muestra nacional
        // de teatro hasta ahora (puesto que por el momento todos los profesionales son de esta)
        emit(if (muestra.isEmpty()) emptyList() else filtroEspecialidades)
    }.catch { it.printStackTrace() }
}