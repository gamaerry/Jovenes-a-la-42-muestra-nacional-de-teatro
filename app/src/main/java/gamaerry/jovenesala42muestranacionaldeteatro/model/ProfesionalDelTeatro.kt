package gamaerry.jovenesala42muestranacionaldeteatro.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import gamaerry.jovenesala42muestranacionaldeteatro.datastructure.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS

@Entity(tableName = NOMBRE_BASE_DE_DATOS)
data class ProfesionalDelTeatro(
    @PrimaryKey
    val id: Int,
    val nombre: String,
    val especialidades: String,
    val descripcion: String,
    val estado: String,
    val urlImagen: String,
    /*val correo: String,
    val facebook: String*/
)
