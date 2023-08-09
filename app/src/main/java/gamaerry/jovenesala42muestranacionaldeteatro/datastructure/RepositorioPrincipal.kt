package gamaerry.jovenesala42muestranacionaldeteatro.datastructure

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import gamaerry.jovenesala42muestranacionaldeteatro.extraerLista
import gamaerry.jovenesala42muestranacionaldeteatro.model.ProfesionalDelTeatro
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

// la función del repositorio es únicamente emitir los
// resultados de las funciones suspendibles del dao
@Singleton
class RepositorioPrincipal
@Inject
constructor() {
    private lateinit var listaCompleta: List<ProfesionalDelTeatro>
    init {
        Firebase.database.getReference("profesionales")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    listaCompleta = dataSnapshot.getValue<List<ProfesionalDelTeatro>>()!!
                    Log.d("midebug", "-------> El valor es: $listaCompleta")
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w(
                        "midebug",
                        "--------> Ha sucedido un error en la recuperacion",
                        error.toException()
                    )
                }
            })
    }

    // es basicamente la ejecucion de una consulta de sql
    fun getListaDeProfesionales(palabrasClave: String) = flow {
        emit(listaCompleta.filter {
            it.descripcion.contains(palabrasClave, true) &&
                    it.nombre.contains(palabrasClave, true) &&
                    it.estado.contains(palabrasClave, true) &&
                    it.especialidades.contains(palabrasClave, true)
        })
    }.catch { it.printStackTrace() }

    // es basicamente la ejecucion de una consulta de sql
    fun getNombrePorId(id: Int) = flow {
        emit(listaCompleta.first { it.id == id }.nombre)
    }.catch { it.printStackTrace() }

    // filtra por partes para obtener una consulta adecuada
    // de acuerdo a los estados especialidades y muestras activas
    fun getProfesionalesPorFiltros(
        estados: List<String>,
        especialidades: List<String>,
        muestra: List<String>
    ) = flow {
        // obtiene en primer lugar una lisa de profesionales de acuerdo a la lista de estados
        // luego de la lista anterior, filtra aquellos cuya alguna de su
        // especialida coincida con alguna de la lista de especialidades
        val filtroEspecialidades =
            listaCompleta.filter { it.estado in estados }.filter { profesional ->
                profesional.especialidades.extraerLista().any { it in especialidades }
            }
        // finalmente se emitira una lista vacia si esta desmarcada la unica muestra nacional
        // de teatro hasta ahora (puesto que por el momento todos los profesionales son de esta)
        emit(if (muestra.isEmpty()) emptyList() else filtroEspecialidades)
    }.catch { it.printStackTrace() }
}