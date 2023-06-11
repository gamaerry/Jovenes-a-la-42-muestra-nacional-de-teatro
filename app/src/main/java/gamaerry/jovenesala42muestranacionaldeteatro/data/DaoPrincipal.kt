package gamaerry.jovenesala42muestranacionaldeteatro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

@Dao
interface DaoPrincipal {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun operacionInsertarProfesional(profesional: ProfesionalDelTeatro)

    @Query("SELECT * FROM ProfesionalDelTeatro WHERE nombre LIKE '%' || :palabrasClave || '%'" +
            " OR especialidades LIKE '%' || :palabrasClave || '%'" +
            " OR descripcion LIKE '%' || :palabrasClave || '%'" +
            " OR estado LIKE '%' || :palabrasClave || '%'")
    suspend fun operacionGetListaDeProfesionales(palabrasClave: String): List<ProfesionalDelTeatro>

    @Query("SELECT * FROM ProfesionalDelTeatro WHERE id = :id")
    suspend fun operacionGetProfesionalPorId(id: String): ProfesionalDelTeatro?
}