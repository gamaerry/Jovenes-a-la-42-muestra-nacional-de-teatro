package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import androidx.room.Dao
import androidx.room.Query
import gamaerry.jovenesala42muestranacionaldeteatro.datastructure.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

@Dao
interface DaoPrincipal {
    // busca y regresa al profesional con el id especificado
    @Query("SELECT * FROM $NOMBRE_BASE_DE_DATOS WHERE id = :id")
    suspend fun operacionGetProfesionalPorId(id: String): ProfesionalDelTeatro?

    // busca y regresa al profesional con la especialidad especificada
    @Query("SELECT * FROM $NOMBRE_BASE_DE_DATOS WHERE especialidades LIKE '%' || :especialidad || '%'")
    suspend fun operacionGetProfesionalesPorEspecialidad(especialidad: String): List<ProfesionalDelTeatro>

    // busca y regresa una lista de profesionales que cumpla con el criterio de la consulta
    // (que o la especialidad o la descripcion o el estado coincidan con las palabras clave pasadas)
    @Query(
        "SELECT * FROM $NOMBRE_BASE_DE_DATOS WHERE nombre LIKE '%' || :palabrasClave || '%'" +
                " OR especialidades LIKE '%' || :palabrasClave || '%'" +
                " OR descripcion LIKE '%' || :palabrasClave || '%'" +
                " OR estado LIKE '%' || :palabrasClave || '%'"
    )
    suspend fun operacionGetListaDeProfesionales(palabrasClave: String): List<ProfesionalDelTeatro>

    @Query("SELECT * FROM $NOMBRE_BASE_DE_DATOS WHERE estado IN (:estados) AND especialidades IN (:especialidades)")
    suspend fun operacionGetProfesionalesPorFiltros(estados: List<String>, especialidades: List<String>): List<ProfesionalDelTeatro>
}