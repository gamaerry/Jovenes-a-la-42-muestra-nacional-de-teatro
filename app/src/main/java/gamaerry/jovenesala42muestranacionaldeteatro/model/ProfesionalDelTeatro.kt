package gamaerry.jovenesala42muestranacionaldeteatro.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class ProfesionalDelTeatro(
    @PrimaryKey(autoGenerate = false)
    val id: String = UUID.randomUUID().toString(),
    val nombre: String,
    val especialidades: String,
    val descripcion: String,
    val estado: String,
    val urlImagen: String
)
