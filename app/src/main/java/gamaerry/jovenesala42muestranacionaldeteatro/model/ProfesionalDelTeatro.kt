package gamaerry.jovenesala42muestranacionaldeteatro.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ProfesionalDelTeatro(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val nombre: String,
    val especialidades: String,
    val descripcion: String,
    val estado: String,
    val urlImagen: String
)
