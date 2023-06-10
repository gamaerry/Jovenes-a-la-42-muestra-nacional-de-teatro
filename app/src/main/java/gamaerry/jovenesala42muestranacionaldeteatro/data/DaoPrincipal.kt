package gamaerry.jovenesala42muestranacionaldeteatro.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

// interfaz que define las interacciones (operaciones)
// necesarias con la base de datos de la libreria de Room
@Dao
interface DaoPrincipal {
    // insertar una nota creada a la base de datos
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun operacionInsertarProfesional(profesional: ProfesionalDelTeatro)

    // busca y regresa una lista de notas que cumplan con el criterio de la consulta
    // (en este caso que o contenido o titulo coincidan con las palabras clave pasadas)
    @Query("SELECT * FROM ProfesionalDelTeatro WHERE nombre LIKE '%' || :palabrasClave || '%' " +
            "OR especialidades LIKE '%' || :palabrasClave || '%'" +
            "OR descripcion LIKE '%' || :palabrasClave || '%'" +
            "OR estado LIKE '%' || :palabrasClave || '%'")
    suspend fun operacionGetListaDeProfesionales(palabrasClave: String): List<ProfesionalDelTeatro>

    // busca y regresa la nota con el id especificado
    @Query("SELECT * FROM ProfesionalDelTeatro WHERE id = :id")
    suspend fun operacionGetProfesionalPorId(id: String): ProfesionalDelTeatro?
}