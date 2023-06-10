package gamaerry.jovenesala42muestranacionaldeteatro.data

import android.content.Context
import androidx.room.Room
import gamaerry.jovenesala42muestranacionaldeteatro.data.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

// el dao es un objeto mediante el cual realizamos operaciones elementales
// y el resultado de esas operaciones las va a recibir este repositorio
// que actuara como intermediario entre dichas operaciones y los viewModel
// necesariamente en forma de flows
class RepositorioPrincipal(contexto: Context) {
    private val daoPrincipal: DaoPrincipal = Room
        .databaseBuilder(contexto, BaseDeDatosPrincipal::class.java, NOMBRE_BASE_DE_DATOS)
        .fallbackToDestructiveMigration().build().getDao()

    fun insertarNota(profesional: ProfesionalDelTeatro) = flow {
        emit(daoPrincipal.operacionInsertarProfesional(profesional))
    }.catch { it.printStackTrace() }

    // el view model que llama a esta funcion lo hace
    // mediante una especie de bloque suspendible (.onEach)
    fun getListaDeProfesionales(palabrasClave: String) = flow {
        emit(daoPrincipal.operacionGetListaDeProfesionales(palabrasClave))
    }.catch { it.printStackTrace() }

    // emite el objeto Profesional obtenido por el Dao dada su id
    fun getProfesionalPorId(id: String) = flow {
        if (id == "")
            emit(null)
        else
            emit(daoPrincipal.operacionGetProfesionalPorId(id))
    }.catch { it.printStackTrace() }
}