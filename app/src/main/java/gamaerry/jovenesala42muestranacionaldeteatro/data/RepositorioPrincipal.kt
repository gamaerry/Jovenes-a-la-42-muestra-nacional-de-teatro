package gamaerry.jovenesala42muestranacionaldeteatro.data

import android.content.Context
import androidx.room.Room
import gamaerry.jovenesala42muestranacionaldeteatro.data.BaseDeDatosPrincipal.Companion.NOMBRE_BASE_DE_DATOS
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class RepositorioPrincipal(private val daoPrincipal: DaoPrincipal) {
    fun insertarProfesional(profesional: ProfesionalDelTeatro) = flow {
        emit(daoPrincipal.operacionInsertarProfesional(profesional))
    }.catch { it.printStackTrace() }

    fun getListaDeProfesionales(palabrasClave: String) = flow {
        emit(daoPrincipal.operacionGetListaDeProfesionales(palabrasClave))
    }.catch { it.printStackTrace() }

    fun getProfesionalPorId(id: String) = flow {
        if (id == "")
            emit(null)
        else
            emit(daoPrincipal.operacionGetProfesionalPorId(id))
    }.catch { it.printStackTrace() }
}