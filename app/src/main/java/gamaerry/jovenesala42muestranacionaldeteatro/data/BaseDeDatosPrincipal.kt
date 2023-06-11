package gamaerry.jovenesala42muestranacionaldeteatro.data

import androidx.room.Database
import androidx.room.RoomDatabase
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro

@Database(entities = [ProfesionalDelTeatro::class], version = 1, exportSchema = true)
abstract class BaseDeDatosPrincipal : RoomDatabase() {
    abstract fun getDaoPrincipal(): DaoPrincipal

    companion object {
        const val NOMBRE_BASE_DE_DATOS = "profesionales"
    }
}