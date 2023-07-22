package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import androidx.room.Dao
import androidx.room.Query
import gamaerry.jovenesala42muestranacionaldeteatro.datastructure.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

@Dao
interface DaoPrincipal {
    // busca y regresa una lista de profesionales que cumpla con el criterio de la consulta
    // (que o la especialidad o la descripcion o el estado coincidan con las palabras clave pasadas)
    @Query(
        "SELECT * FROM $NOMBRE_BASE_DE_DATOS WHERE nombre LIKE '%' || :palabrasClave || '%'" +
                " OR especialidades LIKE '%' || :palabrasClave || '%'" +
                " OR descripcion LIKE '%' || :palabrasClave || '%'" +
                " OR estado LIKE '%' || :palabrasClave || '%'"
    )
    suspend fun operacionGetListaDeProfesionales(palabrasClave: String): List<ProfesionalDelTeatro>

    // busca y regresa a cada profesional cuyo estado coincida con alguno de la lista pasada
    @Query("SELECT * FROM $NOMBRE_BASE_DE_DATOS WHERE estado IN (:estados) ")
    suspend fun operacionGetProfesionalesPorEstados(estados: List<String>): List<ProfesionalDelTeatro>

    // busca y regresa al profesional con el id dado
    @Query("SELECT nombre FROM $NOMBRE_BASE_DE_DATOS WHERE id = :id")
    suspend fun operacionGetNombrePorId(id: Int): String?
}