package gamaerry.jovenesala42muestranacionaldeteatro.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import gamaerry.jovenesala42muestranacionaldeteatro.data.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS
import java.util.UUID

@Entity(tableName = NOMBRE_BASE_DE_DATOS)
data class ProfesionalDelTeatro(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val especialidades: String,
    val descripcion: String,
    val estado: String,
    val urlImagen: String
)
