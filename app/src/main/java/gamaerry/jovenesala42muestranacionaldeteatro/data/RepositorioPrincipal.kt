package gamaerry.jovenesala42muestranacionaldeteatro.data

import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositorioPrincipal
@Inject
constructor(private val daoPrincipal: DaoPrincipal) {
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