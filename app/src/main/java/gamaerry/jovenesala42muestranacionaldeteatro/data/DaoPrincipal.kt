package gamaerry.jovenesala42muestranacionaldeteatro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gamaerry.jovenesala42muestranacionaldeteatro.data.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

@Dao
interface DaoPrincipal {
    // inserta a un profesional a la vez a la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun operacionInsertarProfesional(profesional: ProfesionalDelTeatro)

    // busca y regresa al profesional con el id especificado
    @Query("SELECT * FROM profesionales WHERE id = :id")
    suspend fun operacionGetProfesionalPorId(id: String): ProfesionalDelTeatro?

    // busca y regresa una lista de profesionales que cumpla con el criterio de la consulta
    // (que o la especialidad o la descripcion o el estado coincidan con las palabras clave pasadas)
    @Query(
        "SELECT * FROM $NOMBRE_BASE_DE_DATOS WHERE nombre LIKE '%' || :palabrasClave || '%'" +
                " OR especialidades LIKE '%' || :palabrasClave || '%'" +
                " OR descripcion LIKE '%' || :palabrasClave || '%'" +
                " OR estado LIKE '%' || :palabrasClave || '%'"
    )
    suspend fun operacionGetListaDeProfesionales(palabrasClave: String): List<ProfesionalDelTeatro>
}