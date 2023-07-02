package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import android.util.Log
import gamaerry.jovenesala42muestranacionaldeteatro.extraerLista
import kotlinx.coroutines.flow.asFlow
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

    // puesto que el id es unico, se asegura la emision de un solo profesional
    fun getProfesionalPorId(id: String) = flow {
        emit(daoPrincipal.operacionGetProfesionalPorId(id))
    }.catch { it.printStackTrace() }

    fun getProfesionalesPorEspecialidad(especialidad: String) = flow {
        emit(daoPrincipal.operacionGetProfesionalesPorEspecialidad(especialidad))
    }.catch { it.printStackTrace() }

    fun getProfesionalesPorFiltros(
        estados: List<String>,
        especialidades: List<String>,
        muestra: List<String>
    ) = flow {
        val filtroEstados = daoPrincipal.operacionGetProfesionalesPorEstados(estados)
        val filtroEspecialidades = filtroEstados.filter { profesional ->
            profesional.especialidades.extraerLista().any { it in especialidades }
        }
        emit(if (muestra.isEmpty()) emptyList() else filtroEspecialidades)
    }.catch { it.printStackTrace() }

}