package gamaerry.jovenesala42muestranacionaldeteatro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

// definicion de la base de datos:
// especifica el tipo de entidad
// así como su version y el nombre,
// y la funcion (abstracta) que permite
// la creacion del objeto Dao
// que terminara usando el Repositorio
@Database(entities = [ProfesionalDelTeatro::class], version = 1)
abstract class BaseDeDatosPrincipal : RoomDatabase() {
    abstract fun getDao(): DaoPrincipal

    companion object {
        const val NOMBRE_BASE_DE_DATOS = "nota"
    }
}